package com.fienna.movieapp.view.prelogin

import androidx.navigation.fragment.findNavController
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.databinding.FragmentRegsiterBinding
import com.fienna.movieapp.viewmodel.PreloginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<FragmentRegsiterBinding,PreloginViewModel>(FragmentRegsiterBinding::inflate) {
    override val viewModel: PreloginViewModel by viewModel()

    override fun initView() {
        with(binding){
            btnSignup.text = resources.getString(R.string.btn_signup)
            formEmailSignup.hint = resources.getString(R.string.tv_email)
            formPasswordSignup.hint = resources.getString(R.string.tv_password)
            tvAhaa.text= resources.getString(R.string.tv_login_question)
            tvLogin.text= resources.getString(R.string.tv_login)
            tvTnc.text = resources.getString(R.string.tv_tnc)
        }
    }

    override fun initListener() {
        with(binding){
            tvLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
            btnSignup.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_profileFragment)
            }
        }
    }

    override fun observeData() {}

}