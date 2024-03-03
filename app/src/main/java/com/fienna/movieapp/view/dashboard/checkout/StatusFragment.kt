package com.fienna.movieapp.view.dashboard.checkout

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentStatusBinding
import com.fienna.movieapp.utils.extractYearMonthDate
import com.fienna.movieapp.viewmodel.DashboardViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatusFragment :
    BaseFragment<FragmentStatusBinding, DashboardViewModel>(FragmentStatusBinding::inflate) {
    override val viewModel: DashboardViewModel by viewModel()
    val safeArgs: StatusFragmentArgs by navArgs()
    var userIdValue: String = ""
    var movieId = ""

    private lateinit var database: DatabaseReference
    override fun initView() {
        with(binding) {
            tvTitleStatus.text = resources.getString(R.string.tv_status)
            tvPaymentIsSuccessful.text = resources.getString(R.string.tv_payment_is_successful)
            tvTransactionDetail.text = resources.getString(R.string.tv_transaction_detail)
            tvTransactionId.text = resources.getString(R.string.tv_transaction_date)
            tvMovieTitle.text = resources.getString(R.string.tv_movie_title)
            tvTotalPayment.text = resources.getString(R.string.tv_total_payment)
        }

        safeArgs.movieId.let {
            movieId = it
        }
        database = Firebase.database.reference

        viewModel.getUserId()
        readMovieFromDatabase()
    }

    override fun initListener() {
        binding.cvBackStatus.setOnClickListener {
            findNavController().navigate(R.id.action_statusFragment_to_dashboardFragment)
        }
    }

    override fun observeData() {
        viewModel.run {
            userId.launchAndCollectIn(viewLifecycleOwner) {
                userIdValue = it
            }
        }
    }

    private fun readMovieFromDatabase() {
        viewModel.getMovieFromDatabase(userIdValue, movieId).launchAndCollectIn(viewLifecycleOwner) { data ->
            with(binding) {
               tvTransactionDateValue.text = extractYearMonthDate(data?.transactionTime)
                tvMovieTitleValue.text = data?.titleMovie
                tvTotalPaymentValue.text = data?.priceMovie.toString()
            }
        }
    }

}