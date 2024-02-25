package com.fienna.movieapp.view.onboarding

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.databinding.FragmentSplashScreenBinding
import com.fienna.movieapp.viewmodel.OnboardingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenFragment : BaseFragment<FragmentSplashScreenBinding, OnboardingViewModel>(FragmentSplashScreenBinding::inflate) {
    override val viewModel: OnboardingViewModel by viewModel()
    override fun initView() {
        animateSplash()
    }

    override fun initListener() {
    }

    override fun observeData() {
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