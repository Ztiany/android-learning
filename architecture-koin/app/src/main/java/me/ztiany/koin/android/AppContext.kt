package me.ztiany.koin.android

import me.ztiany.koin.android.common.BaseAppContext
import me.ztiany.koin.android.isolated.IsolatedModule
import me.ztiany.koin.android.main.MainActivity
import me.ztiany.koin.android.main.MainPresenter
import org.koin.dsl.module

/**
 *@author Ztiany
 */
class AppContext : BaseAppContext() {

    override fun onCreate() {
        super.onCreate()
        IsolatedModule.init(this)
    }

    override fun provideSubModules() = arrayOf(
        module {
            scope<MainActivity> {
                scoped { MainPresenter() }
            }
        }
    )

}