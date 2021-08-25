package me.ztiany.mmkv.simulation

import java.io.File

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-07-24 17:35
 */
fun File.createNew() {
    parentFile?.mkdirs()
    createNewFile()
}