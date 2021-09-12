package me.ztiany.compose.wechat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class WeViewModel : ViewModel() {

    var selectedTab by mutableStateOf(0)

}