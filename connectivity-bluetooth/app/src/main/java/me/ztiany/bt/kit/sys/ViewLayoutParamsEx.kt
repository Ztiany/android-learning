@file:JvmName("ViewLayoutParamsEx")

package me.ztiany.bt.kit.sys

import android.view.ViewGroup

fun newWWLayoutParams(): ViewGroup.LayoutParams {
    return ViewGroup.LayoutParams(
        ViewGroup.MarginLayoutParams.WRAP_CONTENT,
        ViewGroup.MarginLayoutParams.WRAP_CONTENT
    )
}

fun newWMLayoutParams(): ViewGroup.LayoutParams {
    return ViewGroup.LayoutParams(
        ViewGroup.MarginLayoutParams.WRAP_CONTENT,
        ViewGroup.MarginLayoutParams.MATCH_PARENT
    )
}

fun newMWLayoutParams(): ViewGroup.LayoutParams {
    return ViewGroup.LayoutParams(
        ViewGroup.MarginLayoutParams.MATCH_PARENT,
        ViewGroup.MarginLayoutParams.WRAP_CONTENT
    )
}

fun newMMLayoutParams(): ViewGroup.LayoutParams {
    return ViewGroup.LayoutParams(
        ViewGroup.MarginLayoutParams.MATCH_PARENT,
        ViewGroup.MarginLayoutParams.MATCH_PARENT
    )
}

fun newWWMarginLayoutParams(): ViewGroup.MarginLayoutParams {
    return ViewGroup.MarginLayoutParams(
        ViewGroup.MarginLayoutParams.WRAP_CONTENT,
        ViewGroup.MarginLayoutParams.WRAP_CONTENT
    )
}

fun newWMMarginLayoutParams(): ViewGroup.MarginLayoutParams {
    return ViewGroup.MarginLayoutParams(
        ViewGroup.MarginLayoutParams.WRAP_CONTENT,
        ViewGroup.MarginLayoutParams.MATCH_PARENT
    )
}

fun newMWMarginLayoutParams(): ViewGroup.MarginLayoutParams {
    return ViewGroup.MarginLayoutParams(
        ViewGroup.MarginLayoutParams.MATCH_PARENT,
        ViewGroup.MarginLayoutParams.WRAP_CONTENT
    )
}

fun newMMMarginLayoutParams(): ViewGroup.MarginLayoutParams {
    return ViewGroup.MarginLayoutParams(
        ViewGroup.MarginLayoutParams.MATCH_PARENT,
        ViewGroup.MarginLayoutParams.MATCH_PARENT
    )
}