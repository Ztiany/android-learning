package com.ztiany.androidx.kotlin.coroutines.official.google.flow

import android.os.SystemClock.sleep
import kotlin.concurrent.thread
import kotlin.random.Random

interface UserLoadedCallback {
    fun onUserLoaded(name: String)
    fun onErrorOccurred(throwable: Throwable)
}

interface UserLoadController {
    fun cancel()
}

fun loadUser(userLoadedCallback: UserLoadedCallback): UserLoadController {
    thread {
        sleep(1000)
        if (Random.nextBoolean()) {
            userLoadedCallback.onUserLoaded("Alien")
        } else {
            userLoadedCallback.onErrorOccurred(NullPointerException())
        }
    }

    return object : UserLoadController {
        override fun cancel() {

        }
    }
}