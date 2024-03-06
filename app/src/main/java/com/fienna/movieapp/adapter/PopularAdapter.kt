package com.fienna.movieapp.adapter

import android.view.View
import coil.load
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseListAdapter
import com.fienna.movieapp.core.domain.model.DataPopular
import com.fienna.movieapp.databinding.ItemMovieBinding
import com.fienna.movieapp.utils.AppConstant
import com.fienna.movieapp.utils.changeGenre
import com.fienna.movieapp.utils.extractYearFromDate

class PopularAdapter(
    private val action: (DataPopular) -> Unit
) : BaseListAdapter<DataPopular, ItemMovieBinding>(ItemMovieBinding::inflate) {
    override fun onItemBind(): (DataPopular, ItemMovieBinding, View, Int) -> Unit =
        { data, binding, view, _ ->
            binding.run {
                titleNowPlaying.text = data.title
                yearAndGenreComingSoon.text =
                    view.resources.getString(R.string.year_and_genre_movie)
                        .replace("%year%", extractYearFromDate(data.releaseDate))
                        .replace("%genre%", changeGenre(data.genreIds.first(), view.context))
                imgComingSoon.load(AppConstant.imageLink + data.posterPath)
                tokenComingSoon.text = data.popularity.toInt().toString()


            }
            view.setOnClickListener {
                action.invoke(data)
            }
        }
}
