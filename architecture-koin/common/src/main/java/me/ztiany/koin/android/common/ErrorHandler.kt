package me.ztiany.koin.android.common

import android.content.Context
import android.widget.Toast

/**
 *@author Ztiany
 */
interface ErrorHandler {
    fun handleError(error: Throwable)
}

internal class ErrorHandlerImpl(private val context: Context) : ErrorHandler {

    override fun handleError(error: Throwable) {
        Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
    }

}
