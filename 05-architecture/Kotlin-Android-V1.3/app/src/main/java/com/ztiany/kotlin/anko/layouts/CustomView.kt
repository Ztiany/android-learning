package com.ztiany.kotlin.anko.layouts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.support.annotation.Nullable
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.dip

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-11 22:15
 */
class CustomView constructor(context: Context, @Nullable attrs: AttributeSet?) : View(context, attrs) {

    constructor(context: Context) : this(context, null)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(dip(20), dip((30)))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.RED)
    }

}