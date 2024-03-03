package com.fienna.movieapp.view.dashboard.setting.token

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.fienna.movieapp.R
import com.fienna.movieapp.adapter.TokenAdapter
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.domain.model.DataToken
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentTokenBinding
import com.fienna.movieapp.utils.CustomSnackbar
import com.fienna.movieapp.utils.DateTimeNow
import com.fienna.movieapp.utils.currency
import com.fienna.movieapp.viewmodel.DashboardViewModel
import com.fienna.movieapp.viewmodel.TokenViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TokenFragment :
    BaseFragment<FragmentTokenBinding, TokenViewModel>(FragmentTokenBinding::inflate) {
    override val viewModel: TokenViewModel by viewModel()
    val safeArgs: TokenFragmentArgs by navArgs()
    val dashboardViewModel: DashboardViewModel by viewModel()
    var paymentLabel: String = ""
    var userId: String = ""
    var userName: String = ""

    private val tokenAdapter by lazy {
        TokenAdapter(
            action = {
                setandSendTokenToDatabase(it.token.toInt(), it.price)
            }
        )
    }

    override fun initView() {
        with(binding) {
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
                tvChoosePayment.text = it
                paymentLabel = it
            }
            if (paymentLabel == resources.getString(R.string.tv_choose_payment)) {
                binding.btnBuyToken.isEnabled = false
            } else {
                binding.btnBuyToken.isEnabled = true
            }
        }

        dashboardViewModel.getProfileName()
        dashboardViewModel.getUserId()
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
        with(viewModel) {
            getDataToken()
            getConfigStatusToken().launchAndCollectIn(viewLifecycleOwner) {
                getDataToken()
            }
        }

        dashboardViewModel.userId.launchAndCollectIn(viewLifecycleOwner) {
            userId = it
        }

        dashboardViewModel.profileUserName.launchAndCollectIn(viewLifecycleOwner) {
            userName = it

        }
    }

    private fun getDataToken() {
        viewModel.getConfigToken().launchAndCollectIn(viewLifecycleOwner) {
            tokenAdapter.submitList(it)
        }
    }

    fun writeNewToken(token: Int, price: Int) {
        val dataToken = DataToken(
            paymentMethod = paymentLabel,
            price = price,
            token = token.toString(),
            transactionTime = DateTimeNow(),
            userId = userId,
            userName = userName,
        )
        viewModel.sendDataToDatabase(dataToken, userId)
            .launchAndCollectIn(viewLifecycleOwner) { success ->
                if (success) {
                    println("MASUK SUKSES SEND")
                    context?.let {
                        CustomSnackbar.showSnackBarWithAction(
                            it,
                            binding.root,
                            "${dataToken.token} ${resources.getString(R.string.tv_added_token)}",
                            action = {
                                findNavController().navigate(R.id.action_tokenFragment_to_settingFragment)
                            }
                        )
                    }
                }

            }
    }

    fun setandSendTokenToDatabase(token: Int, price: Int) {
        binding.tvTokenPrice.text = currency(price)
        binding.tvBuyTokenAmount.text = token.toString()
        binding.btnBuyToken.setOnClickListener {
            writeNewToken(token, price)
        }
    }
}