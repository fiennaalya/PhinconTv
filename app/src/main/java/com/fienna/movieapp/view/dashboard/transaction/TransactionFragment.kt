package com.fienna.movieapp.view.dashboard.transaction

import androidx.recyclerview.widget.LinearLayoutManager
import com.fienna.movieapp.adapter.TransactionAdapter
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.domain.model.DataMovieTransaction
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentTransactionBinding
import com.fienna.movieapp.viewmodel.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionFragment : BaseFragment<FragmentTransactionBinding, DashboardViewModel>(FragmentTransactionBinding::inflate) {
    override val viewModel: DashboardViewModel by viewModel()
    private val list = mutableListOf<DataMovieTransaction>()
    private val transactionAdapter = TransactionAdapter()
    var userIdToken = ""
    override fun initView() {
        binding.rvTransaction.run {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
            hasFixedSize()
        }
        viewModel.getUserId()
    }

    override fun initListener() {
    }

    override fun observeData() {
        viewModel.userId.launchAndCollectIn(viewLifecycleOwner) {
            userIdToken = it
        }
        viewModel.getAllMovieFromDatabase(userIdToken).launchAndCollectIn(viewLifecycleOwner){
            list.addAll(it)
            transactionAdapter.submitList(list)
        }
    }

}