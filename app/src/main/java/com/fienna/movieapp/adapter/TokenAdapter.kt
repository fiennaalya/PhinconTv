package com.fienna.movieapp.adapter

import android.view.View
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseListAdapter
import com.fienna.movieapp.core.domain.model.DataToken
import com.fienna.movieapp.databinding.ItemTokenBinding
import com.fienna.movieapp.utils.currency

class TokenAdapter(
    private val action: (DataToken) -> Unit,
) : BaseListAdapter<DataToken, ItemTokenBinding>(ItemTokenBinding::inflate) {
    private var selectedItem : DataToken? = null

    override fun onItemBind(): (DataToken, ItemTokenBinding, View, Int) -> Unit =
        { data, binding, itemView, position ->
            binding.run {
                tvTokenInput.text = data.token
                tvRupiahInput.text = currency(data.price)
                if (data == selectedItem){
                    layoutCardToken.setBackgroundColor(itemView.resources.getColor(R.color.sky_blue))
                }else{
                    layoutCardToken.setBackgroundColor(layoutCardToken.cardBackgroundColor.defaultColor)
                }

                itemView.setOnClickListener {
                    action.invoke(data)
                    selectedItem = data
                    notifyDataSetChanged()
                }
            }
        }
}
