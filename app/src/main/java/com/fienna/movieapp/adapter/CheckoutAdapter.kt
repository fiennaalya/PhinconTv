package com.fienna.movieapp.adapter

import android.view.View
import coil.load
import com.fienna.movieapp.core.base.BaseListAdapter
import com.fienna.movieapp.core.domain.model.DataTransaction
import com.fienna.movieapp.databinding.ItemListCheckoutBinding
import com.fienna.movieapp.utils.AppConstant

class CheckoutAdapter(
    private val action: (DataTransaction) -> Unit
) : BaseListAdapter<DataTransaction, ItemListCheckoutBinding>(ItemListCheckoutBinding::inflate) {
    override fun onItemBind(): (DataTransaction, ItemListCheckoutBinding, View, Int) -> Unit =
        { data, binding, view, _ ->
            binding.run {
                tvCartName.text = data.title
                tvCartPrice.text = data.popularity.toInt().toString()
                imgCartItems.load(AppConstant.imageLink + data.posterPath)
                action.invoke(data)
            }
        }
}
