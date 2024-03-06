package com.fienna.movieapp.adapter

import android.view.View
import coil.load
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BasePagingAdapter
import com.fienna.movieapp.core.domain.model.DataSearch
import com.fienna.movieapp.databinding.ItemSearchMovieBinding
import com.fienna.movieapp.utils.AppConstant
import com.fienna.movieapp.utils.extractYearFromDate
import com.fienna.movieapp.utils.formatRating

class SearchAdapter(
    private val action: (DataSearch) -> Unit
) : BasePagingAdapter<DataSearch, ItemSearchMovieBinding>(ItemSearchMovieBinding::inflate) {
    override fun onItemBind(): (DataSearch, ItemSearchMovieBinding, View, Int) -> Unit =
        { data, binding, itemView, _ ->
            binding.run {
                imgComingSoon.load(AppConstant.imageLink + data.posterPath)
                titleNowPlaying.text = data.title
                yearAndGenreComingSoon.text =
                    itemView.resources.getString(R.string.year)
                        .replace("%year%", extractYearFromDate(data.releaseDate))
                tvRatingAndTotal.text = itemView.resources.getString(R.string.tv_rating)
                    .replace("%rating%", formatRating(data.voteAverage))
                    .replace("%total_rating%", data.voteCount.toString())
                tokenComingSoon.text = data.popularity?.toInt().toString()
                itemView.setOnClickListener {
                    action.invoke(data)
                }
            }
        }
}
