package com.fienna.movieapp.view.dashboard.wishlist

import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.databinding.FragmentWishlistBinding
import com.fienna.movieapp.viewmodel.WishlistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WishlistFragment : BaseFragment<FragmentWishlistBinding,WishlistViewModel >(FragmentWishlistBinding::inflate) {
    override val viewModel: WishlistViewModel by viewModel()

    override fun initView() {}

    override fun initListener() {}

    override fun observeData() {}

}