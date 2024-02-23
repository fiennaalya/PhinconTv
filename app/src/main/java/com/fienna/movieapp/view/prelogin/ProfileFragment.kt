package com.fienna.movieapp.view.prelogin

import androidx.navigation.fragment.findNavController
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.databinding.FragmentProfileBinding
import com.fienna.movieapp.viewmodel.PreloginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding, PreloginViewModel>(FragmentProfileBinding::inflate) {
    override val viewModel: PreloginViewModel by viewModel()

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
            btnUsername.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_dashboardFragment)
            }
        }
    }

    override fun observeData() {}


}