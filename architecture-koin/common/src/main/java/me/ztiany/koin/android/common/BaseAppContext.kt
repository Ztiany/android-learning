package me.ztiany.koin.android.common

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.module
import timber.log.Timber

/**
 *@author Ztiany
 */
abstract class BaseAppContext : Application() {

    private val appModule = module {
        single<Database> { DatabaseImpl() }
        single<ErrorHandler> { ErrorHandlerImpl(get()) }
        single<AppRepository> { AppRepositoryImpl() }
        single<UserManager> { UserManagerImpl(get(), get()) }
    }

    override fun onCreate() {
        super.onCreate()
        //初始化 Timber
        Timber.plant(Timber.DebugTree())
        //初始化 Koin
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BaseAppContext)
            modules(appModule, *provideSubModules())
        }
    }

    abstract fun provideSubModules(): Array<Module>

}