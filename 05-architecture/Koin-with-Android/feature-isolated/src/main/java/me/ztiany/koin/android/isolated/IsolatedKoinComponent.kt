package me.ztiany.koin.android.isolated

import org.koin.core.Koin
import org.koin.core.component.KoinComponent

/**
 *@author Ztiany
 */
interface IsolatedKoinComponent : KoinComponent {

    override fun getKoin(): Koin = IsolatedModule.koin

}