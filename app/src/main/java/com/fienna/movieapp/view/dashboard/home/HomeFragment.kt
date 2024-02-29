package com.fienna.movieapp.view.dashboard.home

import android.os.Handler
import android.os.Looper
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fienna.movieapp.R
import com.fienna.movieapp.adapter.DetailNowPlayingAdapter
import com.fienna.movieapp.adapter.PopularAdapter
import com.fienna.movieapp.adapter.UpComingAdapter
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.domain.model.DataNowPlaying
import com.fienna.movieapp.core.domain.state.onError
import com.fienna.movieapp.core.domain.state.onLoading
import com.fienna.movieapp.core.domain.state.onSuccess
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentHomeBinding
import com.fienna.movieapp.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment :  BaseFragment<FragmentHomeBinding,HomeViewModel>(FragmentHomeBinding::inflate){
    override val viewModel: HomeViewModel by viewModel()
    private lateinit var rvUpcoming: RecyclerView
    private lateinit var rvPopular: RecyclerView
    private lateinit var nowPlayingAdapter: DetailNowPlayingAdapter

    private val listUpComingAdapter by lazy {
        UpComingAdapter{data ->
            val bundle = bundleOf("movieId" to data.id.toString())
            activity?.supportFragmentManager?.findFragmentById(R.id.main_fragment_container)?.findNavController()?.navigate(R.id.action_dashboardFragment_to_detailFragment, bundle)
        }
    }

    private val listPopularAdapter by lazy {
        PopularAdapter{data ->
            val bundle = bundleOf("movieId" to data.id.toString())
            activity?.supportFragmentManager?.findFragmentById(R.id.main_fragment_container)?.findNavController()?.navigate(R.id.action_dashboardFragment_to_detailFragment, bundle)
        }
    }

    override fun initView() {
        with(binding){
            tvComingSoon.text = resources.getString(R.string.tv_coming_soon)
            tvPopular.text = resources.getString(R.string.tv_popular)
        }

        rvUpcoming = binding.rvComingSoon
        rvUpcoming.setHasFixedSize(true)

        rvPopular = binding.rvPopular
        rvPopular.setHasFixedSize(true)

        viewModel.fetchUpcomingMovie()
        viewModel.fetchPopularMovie()
        viewModel.fetchNowPlayingMovie()
        upComingView()
        popularView()
        //autoScrollNowPlaying()
    }

    override fun initListener() {}

    override fun observeData() {
        with(viewModel){
            upComingMovie.launchAndCollectIn(viewLifecycleOwner){ state ->
                state.onLoading {}
                    .onSuccess {
                        listUpComingAdapter.submitList(it)
                    }
                    .onError {
                    }
            }

            popularMovie.launchAndCollectIn(viewLifecycleOwner){state ->
                state.onLoading { }
                    .onSuccess {
                        listPopularAdapter.submitList(it)
                    }.onError {
                    }
            }

            nowPlayingMovie.launchAndCollectIn(viewLifecycleOwner){state ->
                state.onLoading {
                }.onSuccess {
                    setupViewPager(it)

                }
            }
        }
    }

    fun upComingView(){
        rvUpcoming.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = listUpComingAdapter
        }
    }

    fun popularView(){
        rvPopular.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = listPopularAdapter
        }
    }

    private fun setupViewPager(list: List<DataNowPlaying>) {
        with(binding) {
            nowPlayingAdapter = DetailNowPlayingAdapter(list)
            vpNowPlaying.adapter = nowPlayingAdapter
            TabLayoutMediator(tlNowPlaying, vpNowPlaying) { _, _ -> }.attach()
        }
    }
    private fun autoScrollNowPlaying() {
        val handler = Handler(Looper.getMainLooper())
        val viewPager = binding.vpNowPlaying

        val update = object : Runnable {
            override fun run() {
                val currentItem = viewPager.currentItem
                val nextItem = if (currentItem < nowPlayingAdapter.itemCount - 1) currentItem + 1 else 0
                viewPager.setCurrentItem(nextItem, true)

                val delay = 3000L
                handler.postDelayed(this, delay)
            }
        }

        handler.postDelayed(update, 5000L)
    }
}