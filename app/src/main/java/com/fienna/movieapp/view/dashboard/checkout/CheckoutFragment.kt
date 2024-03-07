package com.fienna.movieapp.view.dashboard.checkout

import android.os.Bundle
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
import com.fienna.movieapp.viewmodel.FirebaseViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.androidx.viewmodel.ext.android.viewModel


class CheckoutFragment :
    BaseFragment<FragmentCheckoutBinding, DashboardViewModel>(FragmentCheckoutBinding::inflate) {
    override val viewModel: DashboardViewModel by viewModel()
    val safeArgs: CheckoutFragmentArgs by navArgs()
    var dataTransaction: DataTransaction? = null
    private val firebaseViewModel: FirebaseViewModel by viewModel()

    var tokenMovie: Int = 0
    private var title: String = ""
    private var movieId: Int = 0
    var userName = ""
    var tokenAfterPayment = 0
    var userIdToken = ""

    private val checkoutAdapter by lazy {
        CheckoutAdapter (
            action = {
                title = it.title
                movieId = it.movieId
                dataTransaction = it
                dataTransaction?.userId = userIdToken
            },
            dataPrice = {
                println("masuk data price fragment $it")
                tokenMovie = it
                binding.tvTokenPrice.text = it.toString()
            }
        )
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
        }


        safeArgs.dataListTransaction.let {
            checkoutAdapter.submitList(it.listTransaction)
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

            getMovieTokenFromFirebase(userIdToken).launchAndCollectIn(viewLifecycleOwner) { tokenafterpayment ->
                tokenAfterPayment = tokenafterpayment
            }

            getTokenFromFirebase(userIdToken).launchAndCollectIn(viewLifecycleOwner) {
                val finalTokenValue = it - tokenAfterPayment
                binding.tvTokenValueCheckout.text = finalTokenValue.toString()
                setTopUpState(finalTokenValue)
            }

            profileUserName.launchAndCollectIn(viewLifecycleOwner) {
                userName = it
            }


        }
    }

    override fun onResume() {
        super.onResume()
        val screenName = "Checkout"
        firebaseViewModel.logEvent(
            FirebaseAnalytics.Event.BEGIN_CHECKOUT,
            Bundle().apply { putString("screenName", screenName) }
        )
    }

    private fun setTopUpState(token: Int) {
        binding.btnBuyCheckout.isEnabled = false
        println("masuk set token movie $tokenMovie")
        if (token > tokenMovie) {
            binding.btnBuyCheckout.isEnabled = true
            binding.layoutEnoughBalance.visibility = View.GONE
        } else {
            binding.btnBuyCheckout.isEnabled = false
            binding.layoutEnoughBalance.visibility = View.VISIBLE
            binding.tvTopupToken.setOnClickListener {
                firebaseViewModel.logScreenView("paymentCheckout")
                val bundle =
                    bundleOf("paymentLabel" to resources.getString(R.string.tv_choose_payment))
                findNavController().navigate(R.id.action_checkoutFragment_to_tokenFragment, bundle)
            }
        }
    }

    private fun setMovieToDatabase() {
        val dataMovieTransaction = DataMovieTransaction(
            movieId = movieId,
            userId = userIdToken,
            userName = userName,
            titleMovie = title,
            priceMovie = tokenMovie,
            transactionTime = DateTimeNow()
        )

        viewModel.sendMovieToDatabase(dataMovieTransaction, userIdToken, movieId.toString())
            .launchAndCollectIn(viewLifecycleOwner) { success ->
                if (success) {
                    val bundle = bundleOf(
                        "movieId" to movieId.toString()
                    )
                    findNavController().navigate(
                        R.id.action_checkoutFragment_to_statusFragment,
                        bundle
                    )
                }
            }
    }

}
