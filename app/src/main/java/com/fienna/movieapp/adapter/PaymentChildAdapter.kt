package com.fienna.movieapp.adapter

import android.view.View
import com.fienna.movieapp.core.base.BaseListAdapter
import com.fienna.movieapp.core.domain.model.DataPayment
import com.fienna.movieapp.databinding.ItemPaymentChildBinding

class PaymentChildAdapter(
    private val action: (DataPayment.Item) -> Unit
) : BaseListAdapter<DataPayment.Item, ItemPaymentChildBinding>(ItemPaymentChildBinding::inflate) {
    override fun onItemBind(): (DataPayment.Item, ItemPaymentChildBinding, View, Int) -> Unit =
        { data, binding, itemView, position ->
            binding.apply {
                tvTitlePaymentChild.text = data.label
            }
            itemView.setOnClickListener {
                action.invoke(data)
            }
        }
}
