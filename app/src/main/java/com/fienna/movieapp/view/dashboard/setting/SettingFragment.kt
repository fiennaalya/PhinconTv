package com.fienna.movieapp.view.dashboard.setting

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.fienna.movieapp.R
import com.fienna.movieapp.core.base.BaseFragment
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.databinding.FragmentSettingBinding
import com.fienna.movieapp.utils.checkIf
import com.fienna.movieapp.viewmodel.DashboardViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : BaseFragment<FragmentSettingBinding, DashboardViewModel> (FragmentSettingBinding::inflate) {
    override val viewModel: DashboardViewModel by viewModel()
    private lateinit var auth: FirebaseAuth
    var userIdValue :String =""
    override fun initView() {
        auth = FirebaseAuth.getInstance()
        with(binding) {
            abtSetting.title = resources.getString(R.string.title_appbarSetting)
            tvCardToken.text = resources.getString(R.string.tv_card_token)
            tvCardMode.text = resources.getString(R.string.tv_card_dark_mode)
            tvLang.text = resources.getString(R.string.tv_card_lang)
            tvCardLogout.text = resources.getString(R.string.tv_card_logout)
        }

        viewModel.getThemeValue()
        val isLanguageChecked = viewModel.getLanguageValue()
        if (isLanguageChecked){
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageIn)
            AppCompatDelegate.setApplicationLocales(appLocale)
            binding.switchLang.isChecked = true
        }else{
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageEn)
            AppCompatDelegate.setApplicationLocales(appLocale)
            binding.switchLang.isChecked = false
        }

        viewModel.getUserId()
        readFromDatabase()
    }

    override fun initListener() {
        binding.cardLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_settingFragment_to_loginFragment)
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked){
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                }
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            viewModel.saveThemeValue(isChecked)
        }

        binding.switchLang.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageIn)
                AppCompatDelegate.setApplicationLocales(appLocale)
            } else{
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageEn)
                AppCompatDelegate.setApplicationLocales(appLocale)
            }
            val lang = resources.configuration.locales[0].language
            viewModel.saveLanguageValue(lang)
        }

        binding.abtSetting.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_dashboardFragment)
        }

        binding.cardToken.setOnClickListener {
            val bundle = bundleOf("paymentLabel" to resources.getString(R.string.tv_choose_payment))
            findNavController().navigate(R.id.action_settingFragment_to_tokenFragment, bundle)
        }
    }
    override fun observeData() {
        viewModel.run {
            theme.launchAndCollectIn(viewLifecycleOwner){
                binding.switchTheme.checkIf(it)
            }
            userId.launchAndCollectIn(viewLifecycleOwner){
                userIdValue = it
            }
        }

    }

    private fun readFromDatabase(){
        viewModel.getTokenFromFirebase(userIdValue).launchAndCollectIn(viewLifecycleOwner){tokenValue ->
            binding.tvTokenAmountUser.text = tokenValue.toString()
        }
    }

    companion object{
        const val languageIn = "in"
        const val languageEn = "en"
    }

}