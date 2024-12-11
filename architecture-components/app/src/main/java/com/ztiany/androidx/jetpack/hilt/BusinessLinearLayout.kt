package com.ztiany.androidx.jetpack.hilt

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BusinessLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    @Inject lateinit var businessKit: BusinessViewKit

    init {
        businessKit.doBusiness()
    }

}