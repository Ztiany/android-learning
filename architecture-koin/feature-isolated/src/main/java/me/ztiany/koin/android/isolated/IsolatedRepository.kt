package me.ztiany.koin.android.isolated

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import timber.log.Timber

/**
 *@author Ztiany
 */
internal interface IsolatedRepository {

    fun getConfiguration(): String

}

private class IsolatedRepositoryImpl(
    context: Context
) : IsolatedRepository {

    init {
        Timber.d("me.ztiany.koin.android.isolated.IsolatedRepositoryImpl init with context: $context")
    }

    override fun getConfiguration(): String {
        return "Configuration from me.ztiany.koin.android.isolated.IsolatedRepositoryImpl"
    }

}

internal val dataModule = module {
    single<IsolatedRepository> {
        IsolatedRepositoryImpl(androidContext())
    }
}