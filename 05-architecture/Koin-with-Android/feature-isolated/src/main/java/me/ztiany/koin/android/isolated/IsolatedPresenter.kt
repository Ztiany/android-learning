package me.ztiany.koin.android.isolated

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 *@author Ztiany
 */
interface IsolatedPresenter {
    fun getList(): List<String>
}

private class IsolatedPresenterImpl(
    context: Context,
    id: String
) : IsolatedPresenter {

    init {
        println("IsolatedPresenterImpl init with context: $context, id: $id")
    }

    override fun getList(): List<String> {
        return buildList {
            (0 until 10).forEach {
                add("List $it")
            }
        }
    }

}

internal val presenterModule = module {
    scope<IsolatedActivity> {
        scoped<IsolatedPresenter> { params ->
            IsolatedPresenterImpl(
                androidContext(),
                params.get()
            )
        }
    }
}