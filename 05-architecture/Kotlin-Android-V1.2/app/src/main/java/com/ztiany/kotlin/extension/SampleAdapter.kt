package com.ztiany.kotlin.extension

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ztiany.kotlin.R
import kotlinx.android.synthetic.main.item_extesion.view.*

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-08-27 17:45
 */
class SampleAdapter : RecyclerView.Adapter<SampleAdapter.Holder>() {

    override fun getItemCount() = 10

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_extesion, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.let {
            it.itemView.extension_tv_name.text = "Item-${holder.adapterPosition}"
            it.itemView.extension_tv_age.text = position.toString()
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

}