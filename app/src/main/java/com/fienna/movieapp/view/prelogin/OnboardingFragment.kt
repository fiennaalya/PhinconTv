package com.fienna.movieapp.view.prelogin

import androidx.navigation.fragment.findNavController
import com.fienna.movieapp.R
import com.fienna.movieapp.adapter.SectionsPagerAdapter
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.databinding.FragmentOnboardingBinding
import com.fienna.movieapp.viewmodel.PreLoginViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingFragment :
    BaseFragment<FragmentOnboardingBinding, PreLoginViewModel>(FragmentOnboardingBinding::inflate) {
    override val viewModel: PreLoginViewModel by viewModel()

    override fun initView() {
        with(binding) {
            btnLoginOnboarding.text = resources.getString(R.string.btn_login)
            btnRegisterOnboarding.text = resources.getString(R.string.btn_signup)
        }
    }

    override fun initListener() {
        with(binding) {
            btnLoginOnboarding.setOnClickListener {
                viewModel.saveOnBoardingValue(true)
                findNavController().navigate(R.id.action_onboardingFragment_to_loginFragment)
            }

            btnRegisterOnboarding.setOnClickListener {
                viewModel.saveOnBoardingValue(true)
                findNavController().navigate(R.id.action_onboardingFragment_to_registerFragment)
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.vpOnboarding.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabOnboarding, binding.vpOnboarding) { _, _ -> }.attach()
    }

    override fun observeData() {}
}
