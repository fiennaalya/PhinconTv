package com.fienna.movieapp.view.dashboard

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.window.layout.WindowMetricsCalculator
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentDashboardBinding
import com.fienna.movieapp.viewmodel.AuthViewModel
import com.fienna.movieapp.viewmodel.DashboardViewModel
import com.fienna.movieapp.viewmodel.WishlistViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigationrail.NavigationRailView
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>(FragmentDashboardBinding::inflate) {
    override val viewModel: DashboardViewModel by viewModel()
    val authViewModel:AuthViewModel by viewModel()
    val wishlistViewModel:WishlistViewModel by viewModel()
    private lateinit var navController: NavController
    override fun initView() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragment_dashboard_container) as NavHostFragment
        navController = navHostFragment.navController

        viewModel.getCurrentUser()?.let { user ->
            val uid = user.userId
            val username = user.displayName
            authViewModel.saveProfileName(username)
            authViewModel.saveUserId(uid)
            binding.abtDashboard.subtitle = resources.getString(R.string.subtitle_dashboard)
                .replace("%name%", username)

        }
        wishlistViewModel.setBadge()
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

        val metrics = activity?.let {
            WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(
                it
            )
        }

        val widthDp = metrics?.bounds?.width()?.div(resources.displayMetrics.density)

        if (widthDp != null){
            when{
                widthDp < 600f -> {
                    val bottomNav = binding.bnDashboard as BottomNavigationView
                    bottomNav.setupWithNavController(navController)
                    wishlistViewModel.badge.launchAndCollectIn(viewLifecycleOwner){count ->
                        updateBadge(bottomNav, count)
        }
                }
                widthDp < 840f ->{
                    val navRail = binding.bnDashboard as NavigationRailView
                    navRail.setupWithNavController(navController)
                }
                else -> {
                    val navView = binding.bnDashboard as NavigationView
                    navView.setupWithNavController(navController)
                }
            }
        }
    }

    override fun observeData() {
//        wishlistViewModel.countWishlist.launchAndCollectIn(viewLifecycleOwner){count ->
//            println("masuk count dashboard $count")
//            updateBadge(binding.bnDashboard, count)
//        }

    }

    private fun updateBadge(bottomNav:BottomNavigationView ,  count: Int) {
        val wishlistMenuItemId = R.id.wishlistFragment
        val badge = bottomNav.getOrCreateBadge(wishlistMenuItemId)
        badge.isVisible = count > 0
        badge.number = count
    }

}