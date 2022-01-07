package me.ztiany.compose.foundation.sideeffect

interface FirebaseAnalytics {

    fun setUserProperty(key: String, value: String)

    companion object {
        fun newDefault(): FirebaseAnalytics = FakeFirebaseAnalytics()
    }

}

private class FakeFirebaseAnalytics : FirebaseAnalytics {
    override fun setUserProperty(key: String, value: String) {

    }
}

data class User(val name: String, val userType: String)
