package com.fienna.movieapp.view.dashboard.checkout

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.fienna.movieapp.R
import com.fienna.movieapp.adapter.CheckoutAdapter
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.domain.model.DataMovieTransaction
import com.fienna.movieapp.core.domain.model.DataTransaction
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentCheckoutBinding
import com.fienna.movieapp.utils.DateTimeNow
import com.fienna.movieapp.viewmodel.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class CheckoutFragment :
    BaseFragment<FragmentCheckoutBinding, DashboardViewModel>(FragmentCheckoutBinding::inflate) {
    override val viewModel: DashboardViewModel by viewModel()
    val safeArgs: CheckoutFragmentArgs by navArgs()
    private val list = mutableListOf<DataTransaction>()
    var dataTransaction: DataTransaction? = null

    var tokenMovie: Int = 0
    private var title: String = ""
    private var movieId: Int = 0
    var userName = ""
    var tokenAfterCheckout = 0
    var userIdToken = ""

    private val checkoutAdapter by lazy {
        CheckoutAdapter {
            binding.tvTokenPrice.text = it.popularity.toInt().toString()
            tokenMovie = it.popularity.toInt()
            title = it.title
            movieId = it.movieId
            dataTransaction = it
            dataTransaction?.userId = userIdToken
        }
    }

    override fun initView() {
        with(binding) {
            tvTitleCheckout.text = resources.getString(R.string.tv_checkout)
            tvCheckoutItems.text = resources.getString(R.string.tv_checkout_items)
            tvPaymentCheckout.text = resources.getString(R.string.tv_payment)
            tvYourTokenCheckout.text = resources.getString(R.string.tv_your_token)
            tvEnoughPayment.text = resources.getString(R.string.tv_checkout_items)
            tvTopupToken.text = resources.getString(R.string.tv_please_top_up)
            btnBuyCheckout.text = resources.getString(R.string.buy)
            tvCheckoutTotalprice.text = resources.getString(R.string.total_bayar)
            tvTokenPrice.text = resources.getString(R.string.tv_zero)

            rvCheckout.run {
                layoutManager = LinearLayoutManager(context)
                adapter = checkoutAdapter
                hasFixedSize()
            }

            btnBuyCheckout.setOnClickListener {
                setMovieToDatabase()
            }

            viewModel.getProfileName()
            viewModel.getUserId()
            viewModel.getTokenFromFirebase(userIdToken).launchAndCollectIn(viewLifecycleOwner) {
                binding.tvTokenValueCheckout.text = it.toString()
            }

        }

        safeArgs.dataTransaction.let {
            list.addAll(listOf(it))
            checkoutAdapter.submitList(list)
        }
    }

    override fun initListener() {
        binding.cvBackCheckout.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun observeData() {
        viewModel.run {
            userId.launchAndCollectIn(viewLifecycleOwner) {
                userIdToken = it
            }

            getTokenFromFirebase(userIdToken).launchAndCollectIn(viewLifecycleOwner) {
                binding.tvTokenValueCheckout.text = it.toString()
                setTopUpState(it)
            }

            profileUserName.launchAndCollectIn(viewLifecycleOwner) {
                userName = it
            }
        }
    }

    private fun setTopUpState(token: Int) {
        binding.btnBuyCheckout.isEnabled = false

        if (token > tokenMovie) {
            binding.btnBuyCheckout.isEnabled = true
            binding.layoutEnoughBalance.visibility = View.GONE
        } else {
            binding.btnBuyCheckout.isEnabled = false
            binding.layoutEnoughBalance.visibility = View.VISIBLE
            binding.tvTopupToken.setOnClickListener {
                val bundle =
                    bundleOf("paymentLabel" to resources.getString(R.string.tv_choose_payment))
                findNavController().navigate(R.id.action_checkoutFragment_to_tokenFragment, bundle)
            }
        }
        tokenAfterCheckout = token - tokenMovie
    }

    private fun setMovieToDatabase(){
        val dataMovieTransaction = DataMovieTransaction(
            movieId = movieId,
            userId = userIdToken,
            userName = userName,
            titleMovie = title,
            priceMovie = tokenMovie,
            transactionTime = DateTimeNow()
        )

        viewModel.sendMovieToDatabase(dataMovieTransaction, userIdToken,movieId.toString())
            .launchAndCollectIn(viewLifecycleOwner) { success ->
                if (success) {
                    val bundle = bundleOf(
                        "movieId" to movieId.toString()
                    )
                    findNavController().navigate(R.id.action_checkoutFragment_to_statusFragment,bundle)
                }
            }
    }

}