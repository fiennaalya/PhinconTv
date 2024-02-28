package com.fienna.movieapp.adapter

import android.view.View
import coil.load
import com.fienna.movieapp.core.base.BaseListAdapter
import com.fienna.movieapp.core.domain.model.DataCart
import com.fienna.movieapp.databinding.ItemCartBinding
import com.fienna.movieapp.utils.AppConstant

class CartAdapter(
    private val action:(DataCart)-> Unit,
    private val remove:(DataCart)->Unit
):BaseListAdapter<DataCart, ItemCartBinding>(ItemCartBinding::inflate) {
    override fun onItemBind(): (DataCart, ItemCartBinding, View, Int) -> Unit =
        {data, binding, itemView, _ ->
            binding.run {
                imgCartItems.load(AppConstant.imageLink + data.posterPath)
                tvCartName.text = data.title
                tvCartPrice.text = data.popularity.toInt().toString()
                tvCartGenre.text = data.genreName
                imgDelete.setOnClickListener {
                    remove.invoke(data)
                }
            }
            itemView.setOnClickListener {
                action.invoke(data)
            }
        }
}