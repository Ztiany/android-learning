package me.ztiany.compose.facility.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.asImmutable(): LiveData<T> {
    return this
}