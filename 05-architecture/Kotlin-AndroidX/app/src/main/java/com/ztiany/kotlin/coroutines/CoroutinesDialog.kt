package com.ztiany.kotlin.coroutines

import android.content.Context
import kotlin.coroutines.suspendCoroutine

suspend fun Context.confirm(title: String, message: String = "") = suspendCoroutine<Boolean> { continuation ->
    continuation.resumeWith(Result.success(true))
}