package me.ztiany.wifi.kit.wifi.transfer.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import me.ztiany.wifi.kit.wifi.Configuration
import me.ztiany.wifi.kit.wifi.transfer.WiFiP2PMessageDecoder
import timber.log.Timber
import kotlin.coroutines.resume

class WiFiP2PServer {

    private var bossGroup: NioEventLoopGroup? = null
    private var workerGroup: NioEventLoopGroup? = null
    private var channelFuture: ChannelFuture? = null

    var onMessageListener: ((String) -> Unit)? = null

    private var client: Channel? = null

    suspend fun start(): Result<Int> {
        val serverBootstrap = with(ServerBootstrap()) {
            group(
                NioEventLoopGroup().apply {
                    bossGroup = this
                },
                NioEventLoopGroup().apply {
                    workerGroup = this
                })
                .channel(NioServerSocketChannel::class.java)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(object : ChannelInitializer<NioSocketChannel>() {
                    override fun initChannel(ch: NioSocketChannel) {
                        ch.pipeline()
                            .addLast(WiFiP2PMessageDecoder())
                            .addLast(MessageReceiverHandler())
                    }
                })
        }

        withContext(Dispatchers.IO) {
            channelFuture = serverBootstrap.bind(Configuration.PORT)
        }

        return suspendCancellableCoroutine { continuation ->
            channelFuture?.addListener(ChannelFutureListener { future: ChannelFuture ->
                if (future.isSuccess) {
                    Timber.d("端口[${Configuration.PORT}]绑定成功!")
                    continuation.resume(Result.success(0))
                } else {
                    Timber.e(future.cause(), "端口[${Configuration.PORT}]绑定失败!")
                    continuation.resume(Result.failure(Exception("端口[${Configuration.PORT}]绑定失败!")))
                }
            })
        }
    }

    suspend fun stop() {
        withContext(Dispatchers.IO) {
            runCatching {
                channelFuture?.channel()?.close()?.sync()
            }.onFailure {
                Timber.e(it, "channel close failed")
            }
            runCatching {
                bossGroup?.shutdownGracefully()?.sync()
            }.onFailure {
                Timber.e(it, "bossGroup shutdownGracefully failed")
            }
            runCatching {
                workerGroup?.shutdownGracefully()?.sync()
            }.onFailure {
                Timber.e(it, "workerGroup shutdownGracefully failed")
            }
        }

        bossGroup = null
        workerGroup = null
        channelFuture = null
        client = null
    }

    fun send(message: String): Result<Int> {
        if (message.isEmpty()) {
            return Result.failure(Exception("message is empty"))
        }

        val encodedMessage = try {
            WiFiP2PMessageDecoder.encodeMessage(message)
        } catch (e: Exception) {
            return Result.failure(e)
        }

        val channel = client ?: return Result.failure(Exception("channel is null"))

        return try {
            val byteBuf = Unpooled.copiedBuffer(encodedMessage.toByteArray())
            channel.writeAndFlush(byteBuf)
            Result.success(0)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private inner class MessageReceiverHandler : ChannelInboundHandlerAdapter() {

        override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
            client = ctx.channel()
            val message = msg as ByteBuf
            message.toString(Charsets.UTF_8).apply {
                Timber.d("收到消息: $this")
                onMessageListener?.invoke(this)
            }
        }

    }

}