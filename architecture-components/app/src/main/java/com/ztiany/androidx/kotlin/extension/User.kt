package com.ztiany.androidx.kotlin.extension

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-08-27 17:29
 */
@Parcelize
@SuppressLint("ParcelCreator")
data class User(val firstName: String, val lastName: String) : Parcelable
