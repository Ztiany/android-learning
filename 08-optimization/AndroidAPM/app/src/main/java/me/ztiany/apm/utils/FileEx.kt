package me.ztiany.apm.utils

import java.io.File

fun File.readText(): String {
    return readBytes().decodeToString()
}