package com.fienna.movieapp.view.auth

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.domain.state.onCreated
import com.fienna.movieapp.core.domain.state.onValue
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentRegsiterBinding
import com.fienna.movieapp.utils.CustomSnackbar
import com.fienna.movieapp.utils.setText
import com.fienna.movieapp.viewmodel.AuthViewModel
import com.fienna.movieapp.viewmodel.FirebaseViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<FragmentRegsiterBinding,AuthViewModel>(FragmentRegsiterBinding::inflate) {
    override val viewModel: AuthViewModel by viewModel()
    private val firebaseViewModel : FirebaseViewModel by viewModel()
    override fun initView() {
        with(binding){
            btnSignup.text = resources.getString(R.string.btn_signup)
            formEmailSignup.hint = resources.getString(R.string.tv_email)
            formPasswordSignup.hint = resources.getString(R.string.tv_password)
            tvAhaa.text= resources.getString(R.string.tv_login_question)
            tvLogin.text= resources.getString(R.string.tv_login)
            tvTnc.text = resources.getString(R.string.tv_tnc)
        }

        val lang = resources.configuration.locales[0].language
        context?.let { setText(lang, it, binding.tvTnc, getString(R.string.tv_tnc)) }
    }

    override fun initListener() {
        with(binding){
            tvLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
            tietEmailSignup.doOnTextChanged{text, _, _, _ ->
                viewModel.validateRegisterEmail(text.toString())
            }

            tietPassSignup.doOnTextChanged { text, _, _, _ ->
                viewModel.validateRegisterPass(text.toString())
            }

            btnSignup.setOnClickListener {
                val email = tietEmailSignup.text.toString().trim()
                val password = tietPassSignup.text.toString().trim()

                if (formEmailSignup.isErrorEnabled.not() && formPasswordSignup.isErrorEnabled.not()){
                    viewModel.validateRegisterField(email,password)
                    firebaseViewModel.logScreenView(email)
                }
            }
        }
    }

    override fun observeData() {
        with(viewModel){
            registerEmail.launchAndCollectIn(viewLifecycleOwner) { state ->
                state.onCreated {}
                    .onValue { isValid ->
                        binding.run {
                            formEmailSignup.isErrorEnabled = isValid.not()
                            if (isValid) {
                                formEmailSignup.error = null
                                formEmailSignup.helperText = resources.getString(R.string.helperText_email)
                            } else {
                                formEmailSignup.error = resources.getString(R.string.helperText_email_error)
                                formEmailSignup.helperText = null
                            }
                        }
                    }
            }


            registerPass.launchAndCollectIn(viewLifecycleOwner) { state ->
                state.onCreated {}
                    .onValue { isValid ->
                        binding.run {
                            formPasswordSignup.isErrorEnabled = isValid.not()
                            if (isValid.not()) {
                                formPasswordSignup.error = resources.getString(R.string.helperText_password)
                                formPasswordSignup.helperText = null
                            }

                            if (tietEmailSignup.text?.isEmpty()==false ){
                                if (tietPassSignup.text?.isNotEmpty() == true && isValid) {
                                    btnSignup.isEnabled = true
                                    formPasswordSignup.error = null
                                    formPasswordSignup.helperText = resources.getString(R.string.helperText_password)
                                }
                            } else{
                                btnSignup.isEnabled = false
                            }
                        }
                    }
            }

            registerValidation.launchAndCollectIn(viewLifecycleOwner) { state ->
                state.onCreated { }
                    .onValue { isPass ->
                        binding.run {
                            formEmailSignup.isErrorEnabled = isPass.not()
                            formPasswordSignup.isErrorEnabled = isPass.not()
                            if (isPass) {
                                val email = tietEmailSignup.text.toString()
                                val password = tietPassSignup.text.toString()
                                viewModel.signUp(email,password).launchAndCollectIn(viewLifecycleOwner){
                                    if (it){
                                        findNavController().navigate(R.id.action_registerFragment_to_profileFragment)
                                    }else{
                                        context?.let { it1 ->
                                            CustomSnackbar.showSnackBar(
                                                it1,
                                                binding.root,
                                                resources.getString(R.string.other_email_signup)
                                            )
                                        }
                                    }
                                }

                            } else {
                                formPasswordSignup.error = resources.getString(R.string.helperText_password)
                                context?.let { it1 ->
                                    CustomSnackbar.showSnackBar(
                                        it1,
                                        binding.root,
                                        resources.getString(R.string.register_validation)
                                    )
                                }
                            }
                            resetRegisterValidationState()
                        }
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val screenName = resources.getString(R.string.btn_signup)
        firebaseViewModel.logEvent(
            FirebaseAnalytics.Event.SIGN_UP, Bundle().apply { putString("screenName", screenName)}
        )
    }
}
