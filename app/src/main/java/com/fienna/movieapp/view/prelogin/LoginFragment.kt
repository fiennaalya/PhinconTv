package com.fienna.movieapp.view.prelogin

import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.databinding.FragmentLoginBinding
import com.fienna.movieapp.viewmodel.PreloginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding, PreloginViewModel>(FragmentLoginBinding::inflate) {
    override val viewModel: PreloginViewModel by viewModel()

    override fun initView() {
        with(binding){
            formEmail.hint = resources.getString(R.string.tv_email)
            formPassword.hint = resources.getString(R.string.tv_password)
            btnLogin.text = resources.getString(R.string.btn_login)
            tvDhaa.text= resources.getString(R.string.tv_signup_question)
            tvSignUp.text= resources.getString(R.string.tv_signup)
        }
    }

    override fun initListener() {
        TODO("Not yet implemented")
    }

    override fun observeData() {
        TODO("Not yet implemented")
    }

}