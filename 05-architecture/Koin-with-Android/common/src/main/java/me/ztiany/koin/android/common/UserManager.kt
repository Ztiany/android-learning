package me.ztiany.koin.android.common

/**
 *@author Ztiany
 */
interface UserManager {
    fun getUser(): User?
}

internal class UserManagerImpl(
    private val database: Database,
    private val repository: AppRepository
) : UserManager {

    override fun getUser(): User {
        return User("Ztiany")
    }

}

data class User(val name: String)