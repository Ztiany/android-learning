package me.ztiany.koin.android.common

/**
 *@author Ztiany
 */
interface Database {

    fun queryMessages(): List<String>

}

internal class DatabaseImpl : Database {

    override fun queryMessages(): List<String> {
        return buildList {
            (0 until 10).forEach {
                add("Message $it")
            }
        }
    }

}