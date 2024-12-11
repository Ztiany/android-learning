package me.ztiany.bt.service

import androidx.annotation.IntDef

@IntDef(
    State.SERVER_STOPPED,
    State.SERVER_STARTED,
    State.ADVERTISING_STARTED,
    State.ADVERTISING_STOPPED
)
annotation class State {
    companion object {
        const val SERVER_STOPPED = 1
        const val SERVER_STARTED = 2
        const val ADVERTISING_STARTED = 3
        const val ADVERTISING_STOPPED = 4
    }
}
