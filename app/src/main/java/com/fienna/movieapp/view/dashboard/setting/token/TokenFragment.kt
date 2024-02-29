package com.fienna.movieapp.view.dashboard.setting.token

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.fienna.movieapp.R
import com.fienna.movieapp.adapter.TokenAdapter
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentTokenBinding
import com.fienna.movieapp.viewmodel.TokenViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TokenFragment : BaseFragment<FragmentTokenBinding, TokenViewModel>(FragmentTokenBinding::inflate){
    override val viewModel: TokenViewModel by viewModel()
    val safeArgs: TokenFragmentArgs by navArgs()
    private val tokenAdapter by lazy {
        TokenAdapter(
            action = { it }
        )
    }
    override fun initView() {
        with(binding){
            tvTitleCart.text = resources.getString(R.string.tv_topup)
            tvTitleTokenAmount.text = resources.getString(R.string.tv_card_token)
            tvTitleChooseAmount.text = resources.getString(R.string.tv_choose_amount)
            tvTokenTotalprice.text = resources.getString(R.string.total_bayar)
            tvTokenPrice.text = resources.getString(R.string.harga_detail)
            btnBuyToken.text = resources.getString(R.string.buy)
            tvPayment.text = resources.getString(R.string.tv_payment)

            rvToken.run {
                layoutManager = GridLayoutManager(context, 3)
                adapter = tokenAdapter
                hasFixedSize()
            }

            safeArgs.paymentLabel.let {
                println("MASUK $it")
                tvChoosePayment.text = it
            }

        }
    }

    override fun initListener() {
        binding.cvBackToken.setOnClickListener {
            findNavController().navigate(R.id.action_tokenFragment_to_settingFragment)
        }

        binding.cardPayment.setOnClickListener {
            findNavController().navigate(R.id.action_tokenFragment_to_paymentFragment)
        }
    }

    override fun observeData() {
        with(viewModel){
            getDataToken()
            getConfigStatusToken().launchAndCollectIn(viewLifecycleOwner){
                getDataToken()
            }
        }
    }

    private fun getDataToken(){
        viewModel.getConfigToken().launchAndCollectIn(viewLifecycleOwner){
            tokenAdapter.submitList(it)
        }
    }
}