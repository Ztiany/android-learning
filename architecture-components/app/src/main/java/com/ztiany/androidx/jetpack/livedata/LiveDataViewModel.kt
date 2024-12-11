package com.ztiany.androidx.jetpack.livedata

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class LiveDataViewModel @Inject constructor() : ViewModel() {

    private val userId = MutableLiveData(100)

    val userDetail = userId.switchMap { id ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(queryUser(id))
        }
    }

    private suspend fun queryUser(userId: Int): User {
        delay(1000)
        if (100 == userId) {
            return User("Alien", 30)
        }
        return User("虾粥", 27)
    }

    fun loadUser() = liveData {
        emit(User("虾粥", 27))
    }

    fun setUserId(userId: Int) {
        this.userId.value = userId
    }

}