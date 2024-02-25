package com.fienna.movieapp.view.onboarding

import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.databinding.FragmentItemOnboardingBinding
import com.fienna.movieapp.viewmodel.OnboardingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ItemOnboardingFragment : BaseFragment<FragmentItemOnboardingBinding, OnboardingViewModel>(FragmentItemOnboardingBinding::inflate) {
    override val viewModel: OnboardingViewModel by viewModel()

    override fun initView() {}

    override fun initListener() {
        val image = binding.imgThumbnailOnboarding
        val title = binding.tvTitleThumbnailOnboarding
        val subtitle = binding.tvSubtitleThumbnailOnboarding
        val index = requireArguments().getInt(ARG_SECTION_NUMBER, 0)

        when(index){
            1 -> {
                image.setImageResource(R.drawable.img_movie_first)
                title.text = resources.getString(R.string.tv_thumbnail_onboarding)
                subtitle.text = resources.getString(R.string.tv_subtitle_thumbnail)
            }
            2 -> {
                image.setImageResource(R.drawable.img_movie_third)
                title.text = resources.getString(R.string.tv_thumbnail_onboarding_3)
                subtitle.text = resources.getString(R.string.tv_subtitle_thumbnail_3)
            }
            else -> {
                image.setImageResource(R.drawable.img_movie_second)
                title.text = resources.getString(R.string.tv_thumbnail_onboarding_2)
                subtitle.text = resources.getString(R.string.tv_subtitle_thumbnail_2)
            }
        }
    }

    override fun observeData() {}

    companion object{
        const val ARG_SECTION_NUMBER = "section_number"
    }
}