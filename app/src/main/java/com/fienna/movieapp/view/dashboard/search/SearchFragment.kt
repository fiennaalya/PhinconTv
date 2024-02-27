package com.fienna.movieapp.view.dashboard.search

import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.databinding.FragmentSearchBinding
import com.fienna.movieapp.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<FragmentSearchBinding, HomeViewModel>(FragmentSearchBinding::inflate) {
    override val viewModel: HomeViewModel by viewModel()

    override fun initView() {}

    override fun initListener() {}

    override fun observeData() {}
}