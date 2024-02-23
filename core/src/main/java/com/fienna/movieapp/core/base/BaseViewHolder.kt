package com.fienna.movieapp.core.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseViewHolder <T: Any, VB: ViewBinding>(
    view: View,
    private val binding: VB,
    private val onIntemBind: (T, VB, View, Int) -> Unit
): RecyclerView.ViewHolder(view){
    fun bind(item: T){
        onIntemBind(item, binding, itemView, absoluteAdapterPosition)
    }
}