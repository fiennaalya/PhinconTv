package com.fienna.movieapp.view.dashboard.setting.token

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.fienna.movieapp.R
import com.fienna.movieapp.adapter.TokenAdapter
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentTokenBinding
import com.fienna.movieapp.utils.CustomSnackbar
import com.fienna.movieapp.utils.currency
import com.fienna.movieapp.viewmodel.DashboardViewModel
import com.fienna.movieapp.viewmodel.TokenViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel

class TokenFragment :
    BaseFragment<FragmentTokenBinding, TokenViewModel>(FragmentTokenBinding::inflate) {
    override val viewModel: TokenViewModel by viewModel()
    val safeArgs: TokenFragmentArgs by navArgs()
    val dashboardViewModel: DashboardViewModel by viewModel()
    //var userName : String = ""

    private lateinit var database: DatabaseReference
    private var currentTokenKey: String? = null

    private val tokenAdapter by lazy {
        TokenAdapter(
            action = {
                setandSendTokenToDatabase(it.token.toInt(), it.price.toInt())
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
            }
        }

        database = Firebase.database.reference
        dashboardViewModel.getProfileName()
    }

    override fun initListener() {
        binding.cvBackToken.setOnClickListener {
            findNavController().navigate(R.id.action_tokenFragment_to_settingFragment)
        }

        binding.cardPayment.setOnClickListener {
            findNavController().navigate(R.id.action_tokenFragment_to_paymentFragment)
        }

    }

    private fun getDataToken() {
        viewModel.getConfigToken().launchAndCollectIn(viewLifecycleOwner) {
            tokenAdapter.submitList(it)
        }
    }

    @IgnoreExtraProperties
    data class Token(val username: String? = null, val token: Int? = null) {}

    fun writeNewToken(token: Int) {
        dashboardViewModel.profileUserName.launchAndCollectIn(viewLifecycleOwner) {
            //userName = it
            val tokenValue = Token(it, token)
            val newTokenRef = database.child("userToken").push()
            newTokenRef.setValue(tokenValue)

            currentTokenKey = newTokenRef.key
        }
    }

    fun ReadNewToken(tokenKey: String) {
        val tokenRef = database.child("userToken").child(tokenKey)
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val token = snapshot.getValue(Token::class.java)
                token?.token?.let { viewModel.saveTokenValue(it) }
                println("masuk token ${token?.token} ${token?.username}")

                context?.let {
                    CustomSnackbar.showSnackBarWithAction(
                        it,
                        binding.root,
                        "${token?.token} ${resources.getString(R.string.tv_added_token)}",
                        action = {
                            findNavController().navigate(R.id.action_tokenFragment_to_settingFragment)
                        }
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("loadPost:onCancelled ${error.toException()}")
            }

        }

        tokenRef.addListenerForSingleValueEvent(postListener)
    }

    fun setandSendTokenToDatabase(token: Int, price: Int) {
        binding.tvTokenPrice.text = currency(price)
        binding.tvBuyTokenAmount.text = token.toString()
        binding.btnBuyToken.setOnClickListener {
            writeNewToken(token)
            currentTokenKey?.let { key ->
                ReadNewToken(key)
            }
        }
    }

    override fun observeData() {
        with(viewModel) {
            getDataToken()
            getConfigStatusToken().launchAndCollectIn(viewLifecycleOwner) {
                getDataToken()
            }
        }
    }
}