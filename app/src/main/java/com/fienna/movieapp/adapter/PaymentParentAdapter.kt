package com.fienna.movieapp.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.fienna.movieapp.core.base.BaseListAdapter
import com.fienna.movieapp.core.domain.model.DataPayment
import com.fienna.movieapp.databinding.ItemPaymentParentBinding

class PaymentParentAdapter(
    private val action: (DataPayment.Item) -> Unit
) : BaseListAdapter<DataPayment, ItemPaymentParentBinding>(ItemPaymentParentBinding::inflate) {
    override fun onItemBind(): (DataPayment, ItemPaymentParentBinding, View, Int) -> Unit =
        { data, binding, itemView, position ->
            binding.run {
                tvTitlePaymentParent.text = data.title

                val paymentChildAdapter = PaymentChildAdapter(action)
                rvPaymentChild.apply {
                    layoutManager = LinearLayoutManager(root.context)
                    adapter = paymentChildAdapter
                }
                paymentChildAdapter.submitList(data.item)
            }
        }
}
