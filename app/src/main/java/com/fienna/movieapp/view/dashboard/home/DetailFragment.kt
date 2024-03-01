package com.fienna.movieapp.view.dashboard.home

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fienna.movieapp.R
import com.fienna.movieapp.adapter.CastAdapter
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.domain.model.DataCart
import com.fienna.movieapp.core.domain.model.DataDetailMovie
import com.fienna.movieapp.core.domain.model.DataTransaction
import com.fienna.movieapp.core.domain.model.DataWishlist
import com.fienna.movieapp.core.domain.state.onError
import com.fienna.movieapp.core.domain.state.onLoading
import com.fienna.movieapp.core.domain.state.onSuccess
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentDetailBinding
import com.fienna.movieapp.utils.AppConstant
import com.fienna.movieapp.utils.CustomSnackbar
import com.fienna.movieapp.utils.extractYearFromDate
import com.fienna.movieapp.utils.formatRating
import com.fienna.movieapp.viewmodel.DetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment :
    BaseFragment<FragmentDetailBinding, DetailViewModel>(FragmentDetailBinding::inflate) {
    override val viewModel: DetailViewModel by viewModel()
    val safeArgs: DetailFragmentArgs by navArgs()
    private lateinit var rvCast: RecyclerView
    private val listCastAdapter = CastAdapter()
    private var dataTransaction: DataTransaction? = null

    private var listDataTransaction: MutableList<DataTransaction> = mutableListOf()
    override fun initView() {
        with(binding) {
            tvFavorite.text = resources.getString(R.string.tv_favorite)
            tvActor.text = resources.getString(R.string.actor)
            tvCart.text = resources.getString(R.string.tv_cart)
        }
        rvCast = binding.rvCast
        rvCast.setHasFixedSize(true)
        safeArgs.movieId.let { movieId ->
            viewModel.fetchDetailMovie(movieId.toInt())
            viewModel.fetchCreditMovie(movieId.toInt())
        }
        castView()
        updateFavorite()
        updateCart()
    }

    override fun initListener() {
        with(binding) {
            cvBack.setOnClickListener {
                findNavController().popBackStack()
            }

            cbFavorite.setOnClickListener {
                val isChecked = cbFavorite.isChecked
                if (isChecked) {
                    cbFavorite.setButtonDrawable(context?.getDrawable(R.drawable.ic_check))
                    context?.let { context ->
                        CustomSnackbar.showSnackBar(
                            context,
                            binding.root,
                            resources.getString(R.string.tv_snackbar_wishlist)
                        )
                    }
                    viewModel.insertWishlist()
                } else {
                    cbFavorite.setButtonDrawable(context?.getDrawable(R.drawable.ic_favorite))
                    context?.let { context ->
                        CustomSnackbar.showSnackBar(
                            context,
                            binding.root,
                            resources.getString(R.string.tv_snackbar_wishlist_delete)
                        )
                    }
                    viewModel.deleteWishlist()
                }
            }

            cbCart.setOnClickListener {
                val isChecked = cbCart.isChecked
                if (isChecked) {
                    cbCart.setButtonDrawable(context?.getDrawable(R.drawable.ic_check))
                    context?.let { context ->
                        CustomSnackbar.showSnackBar(
                            context,
                            binding.root,
                            resources.getString(R.string.tv_snackbar_cart_add)
                        )
                    }
                    viewModel.insertCart()
                } else {
                    cbCart.setButtonDrawable(context?.getDrawable(R.drawable.ic_add_deepblue))
                    context?.let { context ->
                        CustomSnackbar.showSnackBar(
                            context,
                            binding.root,
                            resources.getString(R.string.tv_snackbar_cart_delete)
                        )
                    }
                }
            }

            btnBuyDetail.setOnClickListener {
//                val bundle =  bundleOf("listdataTransaction" to dataTransaction.toTypedArray())
                val bundle = bundleOf("dataTransaction" to dataTransaction )
                findNavController().navigate(R.id.action_detailFragment_to_checkoutFragment, bundle)
            }
        }
    }

    override fun observeData() {
        with(viewModel) {
            detailMovie.launchAndCollectIn(viewLifecycleOwner) { state ->
                state.onLoading { }
                    .onSuccess {
                        with(binding) {
                            imgDetail.load(AppConstant.backdropLink + it.backdropPath)
                            tvTitle.text = it.title
                            yearAndGenreDetail.text =
                                resources.getString(R.string.year_and_genre_movie)
                                    .replace("%year%", extractYearFromDate(it.releaseDate))
                                    .replace("%genre%", getGenresString(it.genres))
                            tvRatingAndTotal.text = resources.getString(R.string.tv_rating)
                                .replace("%rating%", formatRating(it.voteAverage))
                                .replace("%total_rating%", it.voteCount.toString())
                            tvDetailDesc.text = it.overview
                            tvToken.text = it.popularity.toInt().toString()
                            updateFavorite()
                            updateCart()
                            viewModel.setDataCart(
                                DataCart(
                                    movieId = it.id,
                                    posterPath = it.posterPath,
                                    title = it.title,
                                    genreName = getGenresString(it.genres),
                                    popularity = it.popularity
                                )
                            )

                            viewModel.setDataWishlist(
                                DataWishlist(
                                    movieId = it.id,
                                    posterPath = it.posterPath,
                                    title = it.title,
                                    popularity = it.popularity,
                                    voteAverage = it.voteAverage,
                                    voteCount = it.voteCount
                                )
                            )

                            listDataTransaction.add(DataTransaction(
                                movieId = it.id,
                                posterPath = it.posterPath,
                                title = it.title,
                                popularity = it.popularity
                            ))

                            dataTransaction = DataTransaction(
                                movieId = it.id,
                                posterPath = it.posterPath,
                                title = it.title,
                                popularity = it.popularity
                            )


                        }
                    }
                    .onError {

                    }

            }

            creditMovie.launchAndCollectIn(viewLifecycleOwner) { state ->
                state.onLoading { }
                    .onSuccess {
                        listCastAdapter.submitList(it)
                    }.onError {
                        println("MASUK error credit ${it.message}")
                    }
            }
        }
    }

    private fun getGenresString(genres: List<DataDetailMovie.Genre>): String {
        val maxGenres = 3
        val limitedGenres = genres.take(maxGenres)
        return limitedGenres.joinToString(", ") { it.name }
    }


    private fun updateFavorite() {
        binding.cbFavorite.isChecked = viewModel.checkFavorite(safeArgs.movieId.toInt())
        when (binding.cbFavorite.isChecked) {
            true -> binding.cbFavorite.setButtonDrawable(context?.getDrawable(R.drawable.ic_check))
            false -> {
                binding.cbFavorite.setButtonDrawable(context?.getDrawable(R.drawable.ic_favorite))
            }
        }
    }

    private fun updateCart() {
        binding.cbCart.isChecked = viewModel.checkAdd(safeArgs.movieId.toInt())
        when (binding.cbCart.isChecked) {
            true -> binding.cbCart.setButtonDrawable(context?.getDrawable(R.drawable.ic_check))
            false -> binding.cbCart.setButtonDrawable(context?.getDrawable(R.drawable.ic_add_deepblue))
        }
    }

    fun castView() {
        rvCast.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = listCastAdapter
        }
    }

}