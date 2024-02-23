package com.fienna.movieapp.core.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.viewbinding.ViewBinding

abstract class BasePagingAdapter <T:Any, VB: ViewBinding>(
    private val inflaterFactory: (LayoutInflater, ViewGroup?, Boolean) -> VB
): PagingDataAdapter<T, BaseViewHolder<T, VB>>(BaseItemCallback<T>()){

    abstract fun onItemBind(): (T, VB, View, Int) -> Unit
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T, VB> {
        val binding = inflaterFactory(LayoutInflater.from(parent.context), parent, false)
        val view = binding.root
        return BaseViewHolder(view, binding, onItemBind())
    }
    override fun onBindViewHolder(holder: BaseViewHolder<T, VB>, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }
}