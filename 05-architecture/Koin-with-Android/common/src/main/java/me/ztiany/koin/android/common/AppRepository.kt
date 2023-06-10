package me.ztiany.koin.android.common

/**
 *@author Ztiany
 */
interface AppRepository {

    fun getConfiguration(): String

}

internal class AppRepositoryImpl : AppRepository {

    override fun getConfiguration(): String {
        return "Configuration from AppRepositoryImpl"
    }

}
