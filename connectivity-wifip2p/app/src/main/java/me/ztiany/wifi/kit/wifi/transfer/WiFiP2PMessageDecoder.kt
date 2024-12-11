package me.ztiany.wifi.kit.wifi.transfer

import io.netty.buffer.Unpooled
import io.netty.handler.codec.DelimiterBasedFrameDecoder


class WiFiP2PMessageDecoder : DelimiterBasedFrameDecoder(100, true, true, Unpooled.copiedBuffer(DIVIDER.toByteArray())) {

    companion object {
        private const val DIVIDER = "####"
        private const val MAX_FRAME_LENGTH = 100

        fun encodeMessage(message: String): String {
            if (message.length > MAX_FRAME_LENGTH) {
                throw IllegalArgumentException("Message is too long")
            }
            return "$message$DIVIDER"
        }
    }

}
