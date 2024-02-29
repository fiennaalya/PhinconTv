package com.fienna.movieapp.view.dashboard.search

import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fienna.movieapp.adapter.SearchAdapter
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentSearchBinding
import com.fienna.movieapp.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(FragmentSearchBinding::inflate) {
    override val viewModel: SearchViewModel by viewModel()
    private lateinit var rvSearch: RecyclerView

    private val listSearchAdapter by lazy {
        SearchAdapter{}
    }
    override fun initView() {
        rvSearch = binding.rvSearch
        rvSearch.setHasFixedSize(true)
        searchView()

    }

    override fun initListener() {
        binding.tietSearch.doAfterTextChanged { query->
            viewModel.fetchSearchMovie(query.toString()).launchAndCollectIn(viewLifecycleOwner){
                listSearchAdapter.submitData(it)
            }
        }
    }

    override fun observeData() {}

    private fun searchView() {
        rvSearch.run {
            layoutManager  = GridLayoutManager(context, 2)
            adapter = listSearchAdapter
        }
    }
}