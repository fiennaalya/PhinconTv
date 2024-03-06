package com.fienna.movieapp.view.dashboard.home

import android.os.Bundle
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
import com.fienna.movieapp.viewmodel.DashboardViewModel
import com.fienna.movieapp.viewmodel.DetailViewModel
import com.fienna.movieapp.viewmodel.FirebaseViewModel
import com.fienna.movieapp.viewmodel.WishlistViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment :
    BaseFragment<FragmentDetailBinding, DetailViewModel>(FragmentDetailBinding::inflate) {
    override val viewModel: DetailViewModel by viewModel()
    private val firebaseViewModel: FirebaseViewModel by viewModel()
    private val wishlistViewModel: WishlistViewModel by viewModel()
    val dashboardViewModel: DashboardViewModel by viewModel()
    val safeArgs: DetailFragmentArgs by navArgs()
    private lateinit var rvCast: RecyclerView
    private val listCastAdapter = CastAdapter()
    private var dataTransaction: DataTransaction? = null
    var movieIdForFirebase = ""
    var userIdValue = ""
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
            movieIdForFirebase = movieId
            viewModel.fetchDetailMovie(movieId.toInt())
            viewModel.fetchCreditMovie(movieId.toInt())
        }
        castView()
        updateFavorite()
        updateCart()
        setButtonBuy()
        dashboardViewModel.getUserId()
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
                    firebaseViewModel.logScreenView("addMovieToWishlist")
                } else {
                    cbFavorite.setButtonDrawable(context?.getDrawable(R.drawable.ic_favorite))
                    context?.let { context ->
                        CustomSnackbar.showSnackBar(
                            context,
                            binding.root,
                            resources.getString(R.string.tv_snackbar_wishlist_delete)
                        )
                    }

                    viewModel.deleteWishlistDetail()
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
                    firebaseViewModel.logScreenView("addMovieToCart")
                } else {
                    cbCart.setButtonDrawable(context?.getDrawable(R.drawable.ic_add_deepblue))
                    context?.let { context ->
                        CustomSnackbar.showSnackBar(
                            context,
                            binding.root,
                            resources.getString(R.string.tv_snackbar_cart_delete)
                        )
                    }
                    viewModel.deleteCartDetail()
                }
            }

            btnBuyDetail.setOnClickListener {
                val bundle = bundleOf("dataTransaction" to dataTransaction)
                findNavController().navigate(R.id.action_detailFragment_to_checkoutFragment, bundle)
                firebaseViewModel.logScreenView("buyDetail")
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

                            listDataTransaction.add(
                                DataTransaction(
                                    movieId = it.id,
                                    posterPath = it.posterPath,
                                    title = it.title,
                                    popularity = it.popularity
                                )
                            )

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
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val screenName = "Detail"
        firebaseViewModel.logEvent(
            FirebaseAnalytics.Event.VIEW_ITEM,
            Bundle().apply { putString("screenName", screenName) }
        )
    }

    private fun getGenresString(genres: List<DataDetailMovie.Genre>): String {
        val maxGenres = 3
        val limitedGenres = genres.take(maxGenres)
        return limitedGenres.joinToString(", ") { it.name }
    }


    private fun updateFavorite() {
        val checkFav = viewModel.checkFavorite(safeArgs.movieId.toInt())
        if (checkFav == 1){
            binding.cbFavorite.isChecked = true
            binding.cbFavorite.setButtonDrawable(context?.getDrawable(R.drawable.ic_check))
        }else{
            binding.cbFavorite.isChecked = false
            binding.cbFavorite.setButtonDrawable(context?.getDrawable(R.drawable.ic_favorite))
        }
        println("MASUK NILAI CHECK FAV $checkFav")
    }

    private fun updateCart() {
        val checkAdd = viewModel.checkAdd(safeArgs.movieId.toInt())
        if (checkAdd ==1){
            binding.cbCart.isChecked = true
            binding.cbCart.setButtonDrawable(context?.getDrawable(R.drawable.ic_check))
        }else{
            binding.cbCart.isChecked = false
            binding.cbCart.setButtonDrawable(context?.getDrawable(R.drawable.ic_add_deepblue))
        }
    }

    fun castView() {
        rvCast.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = listCastAdapter
        }
    }

    fun setButtonBuy() {
        dashboardViewModel.run {
            userId.launchAndCollectIn(viewLifecycleOwner) {
                userIdValue = it
            }

            getMovieFromDatabase(userIdValue, movieIdForFirebase).launchAndCollectIn(
                viewLifecycleOwner
            ) { data ->
                if (data?.movieId.toString() == movieIdForFirebase) {
                    binding.btnBuyDetail.isEnabled = false
                    binding.btnBuyDetail.setTextColor(resources.getColor(R.color.grey))
                    binding.btnBuyDetail.text = resources.getString(R.string.tv_purchased)
                } else {
                    binding.btnBuyDetail.isEnabled = true
                }
            }
        }
    }
}
