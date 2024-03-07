package com.fienna.movieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fienna.movieapp.R
import com.fienna.movieapp.core.domain.model.DataNowPlaying
import com.fienna.movieapp.databinding.ItemNowPlayingBinding
import com.fienna.movieapp.utils.AppConstant
import com.fienna.movieapp.utils.changeGenre
import com.fienna.movieapp.utils.extractYearFromDate
import com.fienna.movieapp.utils.formatRating

class DetailNowPlayingAdapter(private val list: List<DataNowPlaying>) :
    RecyclerView.Adapter<DetailNowPlayingAdapter.DetailViewHolder>() {
    inner class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemNowPlayingBinding.bind(itemView)
        fun bind(data: DataNowPlaying) {
            println("MASUK ADAPTER $data")
            binding.apply {
                imgNowplaying.load(AppConstant.backdropLink + data.backdropPath)
                yearAndGenreComingSoon.text =
                    itemView.resources.getString(R.string.year_and_genre_movie)
                        .replace("%year%", extractYearFromDate(data.releaseDate))
                        .replace("%genre%", changeGenre(data.genreIds.first(), itemView.context))
                tvNowPlayingTitle.text = data.title
                tvRatingAndTotal.text = itemView.resources.getString(R.string.tv_rating)
                    .replace("%rating%", formatRating(data.voteAverage))
                    .replace("%total_rating%", data.voteCount.toString())

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailNowPlayingAdapter.DetailViewHolder {
        val binding = ItemNowPlayingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DetailViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val currentItem = list[position]
        holder.bind(currentItem)
    }
}
