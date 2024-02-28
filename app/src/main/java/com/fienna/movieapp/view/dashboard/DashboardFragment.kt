package com.fienna.movieapp.view.dashboard

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentDashboardBinding
import com.fienna.movieapp.viewmodel.DashboardViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>(FragmentDashboardBinding::inflate) {
    override val viewModel: DashboardViewModel by viewModel()
    private lateinit var navController: NavController
    override fun initView() {
        viewModel.getProfileName()
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragment_dashboard_container) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNav = binding.bnDashboard as BottomNavigationView
        bottomNav.setupWithNavController(navController)
    }

    override fun initListener() {
        binding.abtDashboard.setOnMenuItemClickListener {menuItem ->
            when(menuItem.itemId){
                R.id.navigation_user -> {
                    findNavController().navigate(R.id.action_dashboardFragment_to_settingFragment)
                    true
                }
                R.id.navigation_cart -> {
                    findNavController().navigate(R.id.action_dashboardFragment_to_cartFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun observeData() {
        with(viewModel){
            profileUserName.launchAndCollectIn(viewLifecycleOwner){
                binding.abtDashboard.subtitle = resources.getString(R.string.subtitle_dashboard)
                    .replace("%name%", it)
            }
        }
    }

}