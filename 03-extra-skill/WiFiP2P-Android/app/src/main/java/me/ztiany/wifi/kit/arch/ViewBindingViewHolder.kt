package me.ztiany.wifi.kit.arch

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 *@author Ztiany
 */
open class ViewBindingViewHolder<VB : ViewBinding>(
    val vb: VB,
) : RecyclerView.ViewHolder(vb.root) {

    fun withVB(action: VB.() -> Unit) {
        with(vb, action)
    }

}
