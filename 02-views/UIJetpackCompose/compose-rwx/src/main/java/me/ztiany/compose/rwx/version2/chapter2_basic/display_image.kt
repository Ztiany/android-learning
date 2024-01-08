package me.ztiany.compose.rwx.version2.chapter2_basic

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import me.ztiany.compose.rwx.R


@Composable
private fun Images() {
    // 本地图片：底层依赖于 drawBitmap
    Image(painterResource(id = R.mipmap.ic_launcher),"Icon")

    // 网络图片：底层依赖于 drawBitmap
    Image(painter = rememberAsyncImagePainter(model = "https://t7.baidu.com/it/u=3522949495,3570538969&fm=193&f=GIF"), contentDescription = "Image loaded by Coil")
}