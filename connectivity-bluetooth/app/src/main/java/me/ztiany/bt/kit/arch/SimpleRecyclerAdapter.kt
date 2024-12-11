package me.ztiany.bt.kit.arch

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * A simple way to build a simple list.
 *
 * @author Ztiany
 */
abstract class SimpleRecyclerAdapter<T, VB : ViewBinding>(
    context: Context,
    data: List<T> = emptyList(),
) : BaseRecyclerAdapter<T, ViewBindingViewHolder<VB>>(context, data) {

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewBindingViewHolder<VB> {
        return ViewBindingViewHolder(provideViewBinding(parent, inflater)).also {
            onViewHolderCreated(it)
        }
    }

    override fun onBindViewHolder(viewHolder: ViewBindingViewHolder<VB>, position: Int) {
        val item = getItem(position)
        if (item != null) {
            onBindItem(viewHolder, item)
        } else {
            onBindNullItem(viewHolder, position)
        }
    }

    protected open fun provideViewBinding(parent: ViewGroup, inflater: LayoutInflater): VB {
        return inflateBindingWithParameterizedType(inflater, parent, false)
    }

    protected open fun onViewHolderCreated(viewHolder: ViewBindingViewHolder<VB>) = Unit

    abstract fun onBindItem(viewHolder: ViewBindingViewHolder<VB>, item: T)

    protected open fun onBindNullItem(viewHolder: ViewBindingViewHolder<VB>, position: Int) = Unit

}