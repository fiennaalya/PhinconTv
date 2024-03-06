package com.fienna.movieapp.view.auth

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.domain.state.onCreated
import com.fienna.movieapp.core.domain.state.onValue
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentLoginBinding
import com.fienna.movieapp.utils.CustomSnackbar
import com.fienna.movieapp.viewmodel.AuthViewModel
import com.fienna.movieapp.viewmodel.FirebaseViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment :
    BaseFragment<FragmentLoginBinding, AuthViewModel>(FragmentLoginBinding::inflate) {
    override val viewModel: AuthViewModel by viewModel()
    private val firebaseViewModel: FirebaseViewModel by viewModel()
    override fun initView() {
        with(binding) {
            formEmail.hint = resources.getString(R.string.tv_email)
            formPassword.hint = resources.getString(R.string.tv_password)
            btnLogin.text = resources.getString(R.string.btn_login)
            tvDhaa.text = resources.getString(R.string.tv_signup_question)
            tvSignUp.text = resources.getString(R.string.tv_signup)
        }
    }


    override fun initListener() {
        with(binding) {
            tvSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            tietEmail.doOnTextChanged { text, _, _, _ ->
                viewModel.validateLoginEmail(text.toString())
            }
            tietPass.doOnTextChanged { text, _, _, _ ->
                viewModel.validateLoginPass(text.toString())
            }

            btnLogin.setOnClickListener {
                val email = tietEmail.text.toString().trim()
                val password = tietPass.text.toString().trim()
                if (formEmail.isErrorEnabled.not() && formPassword.isErrorEnabled.not()) {
                    viewModel.validateLoginField(email, password)
                    firebaseViewModel.logScreenView(email)
                }
            }
        }
    }

    override fun observeData() {
        with(viewModel) {
            loginEmail.launchAndCollectIn(viewLifecycleOwner) { state ->
                state.onCreated {}
                    .onValue { isValid ->
                        binding.run {
                            formEmail.isErrorEnabled = isValid.not()
                            if (isValid) {
                                formEmail.error = null
                                formEmail.helperText =
                                    resources.getString(R.string.helperText_email)
                            } else {
                                formEmail.error =
                                    resources.getString(R.string.helperText_email_error)
                                formEmail.helperText = null
                            }
                        }
                    }
            }

            loginPass.launchAndCollectIn(viewLifecycleOwner) { state ->
                state.onCreated { }
                    .onValue { isValid ->
                        binding.run {
                            formPassword.isErrorEnabled = isValid.not()
                            if (isValid.not()) {
                                formPassword.error =
                                    resources.getString(R.string.helperText_password_error)
                                formPassword.helperText = null
                            }

                            if (tietEmail.text?.isEmpty() == false) {
                                if (tietPass.text?.isNotEmpty() == true && isValid) {
                                    btnLogin.isEnabled = true
                                    formPassword.error = null
                                    formPassword.helperText =
                                        resources.getString(R.string.helperText_password)
                                }
                            } else {
                                btnLogin.isEnabled = false
                            }
                        }
                    }
            }

            loginValidation.launchAndCollectIn(viewLifecycleOwner) { state ->
                state.onCreated {}
                    .onValue { isPass ->
                        binding.run {
                            fetchLogin(isPass)
                        }
                    }
            }
            resetLoginValidationState()
        }
    }

    override fun onResume() {
        super.onResume()
        val screenName = resources.getString(R.string.btn_login)
        firebaseViewModel.logEvent(
            FirebaseAnalytics.Event.LOGIN,
            Bundle().apply { putString("screenName", screenName) }
        )
    }

    fun fetchLogin(isPass: Boolean) = binding.run {
        formEmail.isErrorEnabled = isPass.not()
        formPassword.isErrorEnabled = isPass.not()
        if (isPass) {
            val email = tietEmail.text.toString()
            val password = tietPass.text.toString()
            viewModel.signIn(email, password).launchAndCollectIn(viewLifecycleOwner) {
                if (it) {
                    findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                } else {
                    context?.let { it1 ->
                        CustomSnackbar.showSnackBar(
                            it1,
                            binding.root,
                            resources.getString(R.string.other_email)
                        )
                    }
                }
            }

        } else {
            formPassword.error = resources.getString(R.string.helperText_password_error)
            context?.let { it1 ->
                CustomSnackbar.showSnackBar(
                    it1,
                    binding.root,
                    resources.getString(R.string.register_validation)
                )
            }
        }
    }
}
