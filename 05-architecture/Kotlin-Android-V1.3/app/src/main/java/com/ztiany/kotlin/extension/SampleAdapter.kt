package com.ztiany.kotlin.extension

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ztiany.kotlin.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_extesion.*

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-08-27 17:45
 */
class SampleAdapter : RecyclerView.Adapter<SampleAdapter.Holder>() {

    override fun getItemCount() = 10

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_extesion, parent, false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.let {
            it.extension_tv_name.text = "Item-${holder.adapterPosition}"
            it.extension_tv_age.text = position.toString()
        }
    }

    class Holder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

}