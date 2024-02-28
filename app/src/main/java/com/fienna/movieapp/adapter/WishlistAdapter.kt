package com.fienna.movieapp.adapter

import android.view.View
import coil.load
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseListAdapter
import com.fienna.movieapp.core.domain.model.DataWishlist
import com.fienna.movieapp.databinding.ItemWishlistBinding
import com.fienna.movieapp.utils.AppConstant
import com.fienna.movieapp.utils.formatRating

class WishlistAdapter (
    private val action:(DataWishlist)-> Unit,
    private val remove:(DataWishlist)->Unit
): BaseListAdapter<DataWishlist, ItemWishlistBinding>(ItemWishlistBinding::inflate){
    override fun onItemBind(): (DataWishlist, ItemWishlistBinding, View, Int) -> Unit =
    { data, binding, itemView, _ ->
        binding.run {
            imgWishlist.load(AppConstant.imageLink + data.posterPath)
            tvWishlistName.text = data.title
            tvCartPrice.text = data.popularity.toInt().toString()
            tvRatingAndTotalWishlist.text = itemView.resources.getString(R.string.tv_rating)
                .replace("%rating%", formatRating(data.voteAverage))
                .replace("%total_rating%", data.voteCount.toString())
            imgWishlistDelete.setOnClickListener {
                remove.invoke(data)
            }
        }
        itemView.setOnClickListener {
            action.invoke(data)
        }

    }

}