package com.fienna.movieapp.view.dashboard.home

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fienna.movieapp.R
import com.fienna.movieapp.adapter.CastAdapter
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.domain.model.DataDetailMovie
import com.fienna.movieapp.core.domain.state.onError
import com.fienna.movieapp.core.domain.state.onLoading
import com.fienna.movieapp.core.domain.state.onSuccess
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentDetailBinding
import com.fienna.movieapp.utils.AppConstant
import com.fienna.movieapp.utils.extractYearFromDate
import com.fienna.movieapp.utils.formatRating
import com.fienna.movieapp.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : BaseFragment<FragmentDetailBinding, HomeViewModel>(FragmentDetailBinding::inflate) {
    override val viewModel: HomeViewModel by viewModel()
    val safeArgs: DetailFragmentArgs by navArgs()
    private lateinit var rvCast: RecyclerView
    private val listCastAdapter =  CastAdapter()
    override fun initView() {
        with(binding){
            tvFavorite.text = resources.getString(R.string.tv_favorite)
            tvActor.text = resources.getString(R.string.actor)
            tvCart.text= resources.getString(R.string.tv_cart)
        }
        rvCast = binding.rvCast
        rvCast.setHasFixedSize(true)
        safeArgs.movieId.let { movieId ->
            viewModel.fetchDetailMovie(movieId.toInt())
            viewModel.fetchCreditMovie(movieId.toInt())
        }
        castView()
    }

    override fun initListener() {
        binding.cvBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun observeData() {
        with(viewModel){
            detailMovie.launchAndCollectIn(viewLifecycleOwner){state ->
                state.onLoading {  }
                    .onSuccess {
                        with(binding){
                            imgDetail.load(AppConstant.backdropLink+it.backdropPath)
                            tvTitle.text = it.title
                            yearAndGenreDetail.text = resources.getString(R.string.year_and_genre_movie)
                                .replace("%year%", extractYearFromDate(it.releaseDate))
                                .replace("%genre%" , getGenresString(it.genres))
                            tvRatingAndTotal.text = resources.getString(R.string.tv_rating)
                                .replace("%rating%", formatRating(it.voteAverage))
                                .replace("%total_rating%", it.voteCount.toString())
                            tvDetailDesc.text= it.overview
                            tvToken.text = it.popularity.toInt().toString()
                        }
                    }
                    .onError {

                    }

            }

            creditMovie.launchAndCollectIn(viewLifecycleOwner){state ->
                state.onLoading {  }
                    .onSuccess {
                        println("MASUK credit $it")
                        listCastAdapter.submitList(it)
                    }.onError {
                        println("MASUK error credit ${it.message}")
                    }
            }
        }
    }

    private fun getGenresString(genres: List<DataDetailMovie.Genre>): String {
        return genres.map { it.name }.joinToString(", ")
    }

    fun castView(){
        rvCast.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = listCastAdapter
        }
    }

}