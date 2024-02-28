package com.fienna.movieapp.view.dashboard.transaction

import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.databinding.FragmentTransactionBinding
import com.fienna.movieapp.viewmodel.TransactionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionFragment : BaseFragment<FragmentTransactionBinding, TransactionViewModel>(FragmentTransactionBinding::inflate) {
    override val viewModel: TransactionViewModel by viewModel()

    override fun initView() {}

    override fun initListener() {}

    override fun observeData() {}

}