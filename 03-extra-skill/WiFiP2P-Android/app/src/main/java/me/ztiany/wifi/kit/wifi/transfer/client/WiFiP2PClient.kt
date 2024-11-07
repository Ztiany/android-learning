package me.ztiany.wifi.kit.wifi.transfer.client

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import me.ztiany.wifi.kit.wifi.Configuration
import me.ztiany.wifi.kit.wifi.transfer.WiFiP2PMessageDecoder
import timber.log.Timber
import kotlin.coroutines.resume


class WiFiP2PClient {

    private var workerGroup: NioEventLoopGroup? = null
    private var channelFuture: ChannelFuture? = null

    private var address: String = ""

    var onMessageListener: ((String) -> Unit)? = null

    suspend fun start(serverAddress: String): Result<Int> {
        val bootstrap = Bootstrap()
            .group(NioEventLoopGroup().apply { workerGroup = this })
            .channel(NioSocketChannel::class.java)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(object : ChannelInitializer<NioSocketChannel>() {
                override fun initChannel(ch: NioSocketChannel) {
                    ch.pipeline()
                        .addLast(WiFiP2PMessageDecoder())
                        .addLast(MessageReceiverHandler())
                }
            })

        channelFuture = withContext(Dispatchers.IO) {
            bootstrap.connect(serverAddress, Configuration.PORT)
        }

        return suspendCancellableCoroutine { continuation ->
            channelFuture?.addListener { future ->
                if (future.isSuccess) {
                    Timber.d("连接成功")
                    address = serverAddress
                    continuation.resume(Result.success(0))
                } else {
                    Timber.d(future.cause(), "连接失败")
                    continuation.resume(Result.failure(Exception("连接失败")))
                }
            }
        }
    }

    suspend fun stop() {
        withContext(Dispatchers.IO) {
            address = ""
            channelFuture?.channel()?.close()?.sync()
            workerGroup?.shutdownGracefully()
        }
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

        val channel = channelFuture?.channel() ?: return Result.failure(Exception("channel is null"))

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
            val message = msg as ByteBuf
            message.toString(Charsets.UTF_8).apply {
                Timber.d("收到消息: $this")
                onMessageListener?.invoke(this)
            }
        }

    }

}