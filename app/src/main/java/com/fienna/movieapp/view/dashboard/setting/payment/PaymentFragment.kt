package com.fienna.movieapp.view.dashboard.setting.payment

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fienna.movieapp.R
import com.fienna.movieapp.adapter.PaymentParentAdapter
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentPaymentBinding
import com.fienna.movieapp.viewmodel.TokenViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentFragment : BaseFragment<FragmentPaymentBinding, TokenViewModel>(FragmentPaymentBinding::inflate) {
    override val viewModel: TokenViewModel by viewModel()
    private val paymentParentAdapter by lazy {
        PaymentParentAdapter(
            action = {
                val bundle = bundleOf("paymentLabel" to it.label)
                findNavController().navigate(R.id.action_paymentFragment_to_tokenFragment, bundle)
            }
        )
    }

    override fun initView() {
        binding.tvTitlePayment.text = resources.getString(R.string.tv_payment)
        with(binding){
            rvPayment.run {
                layoutManager = LinearLayoutManager(context)
                adapter = paymentParentAdapter
                hasFixedSize()
            }
        }
    }

    override fun initListener() {
        binding.cvBackPayment.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun observeData() {
        with(viewModel){
            getDataPayment()
            getConfigStatusPayment().launchAndCollectIn(viewLifecycleOwner){
                getDataPayment()
            }
        }


    }

    private fun getDataPayment(){
        viewModel.getConfigPayment().launchAndCollectIn(viewLifecycleOwner){
            paymentParentAdapter.submitList(it)
        }
    }
}