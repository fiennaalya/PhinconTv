package com.fienna.movieapp.view.auth

import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.domain.state.onCreated
import com.fienna.movieapp.core.domain.state.onValue
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentProfileBinding
import com.fienna.movieapp.viewmodel.AuthViewModel
import com.google.firebase.auth.userProfileChangeRequest
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding, AuthViewModel>(FragmentProfileBinding::inflate) {
    override val viewModel: AuthViewModel by viewModel()

    override fun initView() {
        with(binding) {
            tvUsername.text = resources.getString(R.string.tv_username)
            tvUsernameDesc.text = resources.getString(R.string.tv_username_desc)
            formUsername.hint= resources.getString(R.string.username)
            btnUsername.text= resources.getString(R.string.btn_username)
        }
    }

    override fun initListener() {
        with(binding){

            tietUsername.doOnTextChanged{text, _, _, _ ->
                viewModel.validateProfileField(text.toString())
            }
            btnUsername.setOnClickListener {
                val profileUpdates = userProfileChangeRequest {
                    displayName = tietUsername.text.toString()
                }

                if (formUsername.isErrorEnabled.not()){
                    viewModel.updateProfile(profileUpdates).launchAndCollectIn(viewLifecycleOwner){
                        if (it){
                            profileUpdates.displayName?.let { it1 -> viewModel.saveProfileName(it1) }
                            viewModel.getCurrentUser()?.userId?.let { it1 ->
                                viewModel.saveUserId(
                                    it1
                                )
                            }
                            findNavController().navigate(R.id.action_profileFragment_to_dashboardFragment)
                        } else{
                            Toast.makeText(
                                context,
                                "please input username",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else{
                }
            }
        }

    }

    override fun observeData() {
        with(viewModel){
            profileNameValidation.launchAndCollectIn(viewLifecycleOwner){state ->
                state.onCreated {  }
                    .onValue { isPass ->
                        binding.run {
                            formUsername.isErrorEnabled = isPass.not()
                            if (isPass) {
                                btnUsername.isEnabled = true
                                formUsername.error = null
                            } else {
                                btnUsername.isEnabled = false
                                formUsername.error = resources.getString(R.string.helperText_name_error)
                            }
                            resetProfileValidationState()
                        }
                    }
            }
        }
    }

}