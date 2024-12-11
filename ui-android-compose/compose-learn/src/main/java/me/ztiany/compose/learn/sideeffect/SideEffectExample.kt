package me.ztiany.compose.learn.sideeffect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember


@Composable
fun SideEffectExample() {
    val user = User("Alien", "SVIP")
    val analytics = rememberAnalytics(user)
}

@Composable
private fun rememberAnalytics(user: User): FirebaseAnalytics {
    val analytics: FirebaseAnalytics = remember {
        FirebaseAnalytics.newDefault()
    }

    // On every successful composition, update FirebaseAnalytics with
    // the userType from the current User, ensuring that future analytics
    // events have this metadata attached
    SideEffect {
        analytics.setUserProperty("userType", user.userType)
    }

    return analytics
}
