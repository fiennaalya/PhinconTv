package com.fienna.movieapp.view.prelogin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.View
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

    private fun animateSplash() {
        binding.layoutTextSplash.visibility = View.GONE

        val visibilityAnimator = ValueAnimator.ofInt(View.GONE, View.VISIBLE)
        visibilityAnimator.addUpdateListener { animator ->
            val value = animator.animatedValue as Int
            binding.layoutTextSplash.visibility = value
        }
        visibilityAnimator.duration = 1

        val translateLeftAnimator = ObjectAnimator.ofFloat(binding.imgSplash, TRANSLATIONX_ANIMATION, 0f, -150f)
        translateLeftAnimator.duration = 1000

        val translateLeftAnimatorText = ObjectAnimator.ofFloat(binding.layoutTextSplash, TRANSLATIONX_ANIMATION, 0f, -150f)
        translateLeftAnimator.duration = 1000

        val fadeInAnimator = ObjectAnimator.ofFloat(binding.layoutTextSplash, ALPHA_ANIMATION, 0f, 1f)
        fadeInAnimator.duration = 2000

        val animatorSet = AnimatorSet()
        animatorSet.play(translateLeftAnimator).before(visibilityAnimator).with(translateLeftAnimatorText).with(fadeInAnimator)

        animatorSet.start()
    }

    companion object {
        const val ALPHA_ANIMATION = "alpha"
        const val TRANSLATIONX_ANIMATION = "translationX"
    }
}