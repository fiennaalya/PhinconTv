package com.fienna.movieapp.view.prelogin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.domain.state.runDashboard
import com.fienna.movieapp.core.domain.state.runLogin
import com.fienna.movieapp.core.domain.state.runOnboarding
import com.fienna.movieapp.core.domain.state.runProfile
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentSplashScreenBinding
import com.fienna.movieapp.viewmodel.PreLoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : BaseFragment<FragmentSplashScreenBinding, PreLoginViewModel>(FragmentSplashScreenBinding::inflate) {
    override val viewModel: PreLoginViewModel by viewModel()
    override fun initView() {
        animateSplash()
        viewModel.getOnBoardingValue()
    }

    override fun initListener() {
    }

    override fun observeData() {
        with(viewModel){
            onBoarding.launchAndCollectIn(viewLifecycleOwner){splashState ->
                lifecycleScope.launch {
                    delay(2000L)
                    splashState.runOnboarding {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_onboardingFragment)
                    }.runDashboard {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_dashboardFragment)
                    }.runLogin {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
                    }.runProfile {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_profileFragment)
                    }
                }
            }
        }
    }

    private fun animateSplash(){
        val fadeInAnimator = ObjectAnimator.ofFloat(binding.imgSplash, "alpha", 0f, 1f)
        fadeInAnimator.duration = 2000 // Sesuaikan durasi animasi

        // Animasi translasi ke kiri untuk img_splash
        val translateLeftAnimator = ObjectAnimator.ofFloat(binding.imgSplash, "translationX", 0f, -binding.root.resources.getDimensionPixelSize(
            org.koin.android.R.dimen.abc_action_bar_subtitle_bottom_margin_material).toFloat())
        translateLeftAnimator.duration = 2000 // Sesuaikan durasi animasi

        // Animasi fade in lebih cepat untuk layout_text_splash
        val fadeInFastAnimator = ObjectAnimator.ofFloat(binding.layoutTextSplash, "alpha", 0f, 1f)
        fadeInFastAnimator.duration = 1000 // Sesuaikan durasi animasi

        // Menjalankan animasi bersamaan
        val animatorSet = AnimatorSet()
        animatorSet.play(fadeInAnimator)
            .before(translateLeftAnimator)
            .before(fadeInFastAnimator)

        // Memulai animasi
        animatorSet.start()
    }


}