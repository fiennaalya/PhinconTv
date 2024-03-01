package com.fienna.movieapp.view.dashboard.checkout

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.fienna.movieapp.R
import com.fienna.movieapp.adapter.CheckoutAdapter
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.domain.model.DataTransaction
import com.fienna.movieapp.databinding.FragmentCheckoutBinding
import com.fienna.movieapp.viewmodel.TransactionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class CheckoutFragment : BaseFragment<FragmentCheckoutBinding, TransactionViewModel>(FragmentCheckoutBinding::inflate) {
    override val viewModel: TransactionViewModel by viewModel()
    val safeArgs: CheckoutFragmentArgs by navArgs()
    private val list = mutableListOf<DataTransaction>()

    private val checkoutAdapter by lazy {
        CheckoutAdapter{}
    }
    override fun initView() {
        with(binding){
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

    override fun observeData() {}

}