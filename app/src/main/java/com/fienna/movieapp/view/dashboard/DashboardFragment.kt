package com.fienna.movieapp.view.dashboard

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.databinding.FragmentDashboardBinding
import com.fienna.movieapp.viewmodel.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>(FragmentDashboardBinding::inflate) {
    override val viewModel: DashboardViewModel by viewModel()
    private lateinit var navController: NavController
    override fun initView() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragment_dashboard_container) as NavHostFragment
        navController = navHostFragment.navController

        with(binding){
            abtDashboard.subtitle = resources.getString(R.string.subtitle_dashboard)
        }
    }

    override fun initListener() {}

    override fun observeData() {}

}