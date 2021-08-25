package me.ztiany.jetpack.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import me.ztiany.jetpack.TAG

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-09-02 12:46
 */
class AppButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    override fun getSupportBackgroundTintList(): ColorStateList? {
        val supportBackgroundTintList = super.getSupportBackgroundTintList()
        Log.d(TAG, "getSupportBackgroundTintList() called")
        return supportBackgroundTintList
    }

}