package com.fienna.movieapp.view.dashboard.cart

import android.app.AlertDialog
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fienna.movieapp.R
import com.fienna.movieapp.adapter.CartAdapter
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.domain.model.DataCart
import com.fienna.movieapp.core.domain.state.onError
import com.fienna.movieapp.core.domain.state.onLoading
import com.fienna.movieapp.core.domain.state.onSuccess
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentCartBinding
import com.fienna.movieapp.utils.CustomSnackbar
import com.fienna.movieapp.viewmodel.CartViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment :
    BaseFragment<FragmentCartBinding, CartViewModel>(FragmentCartBinding::inflate) {
    override val viewModel: CartViewModel by viewModel()
    private var dataCart: List<DataCart>? = null
    private lateinit var rvCart: RecyclerView
    private val listCartAdapter by lazy {
        CartAdapter(
            action = {
                val bundle = bundleOf("movieId" to it.movieId.toString())
                findNavController().navigate(R.id.action_cartFragment_to_detailFragment, bundle)
            },
            remove = { entity -> removeItemFromCart(entity) },
            checkbox = { id, isChecked ->
                viewModel.updateCheckCart(id, isChecked)
                android.os.Handler(Looper.getMainLooper())
                    .postDelayed({ viewModel.totalPrice() }, 500)

            }
        )
    }

    override fun initView() {
        rvCart = binding.rvCart
        rvCart.setHasFixedSize(true)
        viewModel.fetchCart()
        listCartView()

        with(binding) {
            tvTitleCart.text = resources.getString(R.string.menu_cart)
            tvCartSelect.text = resources.getString(R.string.tv_select_all)
            tvCartTotalprice.text = resources.getString(R.string.total_bayar)
            tvCartPrice.text = resources.getString(R.string.harga_detail)
            btnBuy.text = resources.getString(R.string.buy)
        }
    }

    override fun initListener() {
        binding.cvBackCart.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.checkboxCartAll.setOnClickListener {
            val isChecked = binding.checkboxCartAll.isChecked
            listCartAdapter.setAllChecked(isChecked)
        }
    }

    override fun observeData() {
        with(viewModel) {
            fetchCart().launchAndCollectIn(viewLifecycleOwner) { state ->
                this.launch {
                    state.onLoading { }
                        .onSuccess { data ->
                            dataCart = data
                            listCartAdapter.submitList(data)

                        }.onError {
                            context?.let {
                                CustomSnackbar.showSnackBar(
                                    it,
                                    binding.root,
                                    "Error ${it}"
                                )
                            }
                        }
                }
            }

            totalPrice.launchAndCollectIn(viewLifecycleOwner) {
                binding.tvCartPrice.text = it.toString()
            }
        }
    }

    private fun removeItemFromCart(dataCart: DataCart) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage(resources.getString(R.string.tv_delete_wishlist))
                .setNegativeButton(resources.getString(R.string.tv_no)) { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.tv_yes)) { dialog, which ->
                    viewModel.deleteCart(dataCart)
                }
                .show()
                .getButton(AlertDialog.BUTTON_POSITIVE)
                ?.setTextColor(ContextCompat.getColor(it, R.color.red))
        }
    }

    private fun listCartView() {
        rvCart.run {
            layoutManager = LinearLayoutManager(context)
            adapter = listCartAdapter
        }
    }

}
