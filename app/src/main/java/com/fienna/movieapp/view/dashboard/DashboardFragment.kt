package com.fienna.movieapp.view.dashboard

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.databinding.FragmentDashboardBinding
import com.fienna.movieapp.viewmodel.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>(FragmentDashboardBinding::inflate) {
    override val viewModel: DashboardViewModel by viewModel()
    private lateinit var navController: NavController
    private lateinit var name : String
    override fun initView() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragment_dashboard_container) as NavHostFragment
        navController = navHostFragment.navController

        val user = viewModel.getCurrentUser()
        with(binding){
            abtDashboard.subtitle = user?.let {
                name = it.displayName
                resources.getString(R.string.subtitle_dashboard)
                    .replace("Fienna", name)
            }
            user?.let {
                viewModel.putUserId(it.userId)
            }
        }


    }

    override fun initListener() {
        binding.abtDashboard.setOnMenuItemClickListener {menuItem ->
            when(menuItem.itemId){
                R.id.navigation_user -> {
                    findNavController().navigate(R.id.action_dashboardFragment_to_settingFragment)
                    true
                }
                R.id.navigation_cart -> {
                    true
                }
                else -> false
            }
        }
    }

    override fun observeData() {
    }

}