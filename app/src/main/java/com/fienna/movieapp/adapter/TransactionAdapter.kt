package com.fienna.movieapp.adapter

import android.view.View
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseListAdapter
import com.fienna.movieapp.core.domain.model.DataMovieTransaction
import com.fienna.movieapp.databinding.ItemTransactionBinding
import com.fienna.movieapp.utils.extractYearMonthDate

class TransactionAdapter() :
    BaseListAdapter<DataMovieTransaction, ItemTransactionBinding>(ItemTransactionBinding::inflate) {
    override fun onItemBind(): (DataMovieTransaction, ItemTransactionBinding, View, Int) -> Unit =
        { data, binding, view, _ ->
            binding.run {
                tvDateShopping.text = extractYearMonthDate(data.transactionTime)
                tvMovieTitle.text = data.titleMovie
                tvMovieToken.text = view.resources.getString(R.string.tv_token_transaction_value)
                    .replace("%token%", data.priceMovie.toString())
            }
        }
}
