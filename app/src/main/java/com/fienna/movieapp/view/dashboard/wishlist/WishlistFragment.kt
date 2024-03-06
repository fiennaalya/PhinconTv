package com.fienna.movieapp.view.dashboard.wishlist

import android.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fienna.movieapp.R
import com.fienna.movieapp.adapter.WishlistAdapter
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.domain.model.DataWishlist
import com.fienna.movieapp.core.domain.state.onError
import com.fienna.movieapp.core.domain.state.onLoading
import com.fienna.movieapp.core.domain.state.onSuccess
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentWishlistBinding
import com.fienna.movieapp.utils.CustomSnackbar
import com.fienna.movieapp.viewmodel.WishlistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WishlistFragment :
    BaseFragment<FragmentWishlistBinding, WishlistViewModel>(FragmentWishlistBinding::inflate) {
    override val viewModel: WishlistViewModel by viewModel()
    private var dataWishlist: List<DataWishlist>? = null
    private lateinit var rvWishlist: RecyclerView
    var count: Int = 0
    private val listWishlistAdapter by lazy {
        WishlistAdapter(
            action = {
                val bundle = bundleOf("movieId" to it.movieId.toString())
                activity?.supportFragmentManager?.findFragmentById(R.id.main_fragment_container)
                    ?.findNavController()
                    ?.navigate(R.id.action_dashboardFragment_to_detailFragment, bundle)
            },
            remove = { entity -> removeItemFromWishlist(entity) }
        )
    }

    override fun initView() {
        rvWishlist = binding.rvWishlist
        rvWishlist.setHasFixedSize(true)
        viewModel.fetchWishlist()
        viewModel.countWishlist()
        listWishlistView()
    }

    override fun initListener() {}

    override fun observeData() {
        with(viewModel) {
            fetchWishlist().launchAndCollectIn(viewLifecycleOwner) { state ->
                this.launch {
                    state.onLoading { }
                        .onSuccess { data ->
                            dataWishlist = data
                            listWishlistAdapter.submitList(data)

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


            countWishlist.launchAndCollectIn(viewLifecycleOwner) {
                binding.tvWishlistCountItems.text = resources.getString(R.string.tv_count_wishlist)
                    .replace("%angka%", it.toString())
            }

        }
    }

    private fun removeItemFromWishlist(dataWishlist: DataWishlist) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage(resources.getString(R.string.tv_delete_wishlist))
                .setNegativeButton(resources.getString(R.string.tv_no)) { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.tv_yes)) { dialog, which ->
                    viewModel.deleteWishlist(dataWishlist)
                }
                .show()
                .getButton(AlertDialog.BUTTON_POSITIVE)
                ?.setTextColor(ContextCompat.getColor(it, R.color.red))
        }
    }

    private fun listWishlistView() {
        rvWishlist.run {
            layoutManager = LinearLayoutManager(context)
            adapter = listWishlistAdapter
        }
    }
}
