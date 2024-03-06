package com.fienna.movieapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.fienna.movieapp.core.utils.launchAndCollectIn
import com.fienna.movieapp.view.dashboard.setting.SettingFragment
import com.fienna.movieapp.viewmodel.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: DashboardViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.getThemeValue()
        viewModel.theme.launchAndCollectIn(this) {
            AppCompatDelegate.setDefaultNightMode(
                if (it) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }

        val isLanguageChecked = viewModel.getLanguageValue()
        if (isLanguageChecked) {
            val appLocale: LocaleListCompat =
                LocaleListCompat.forLanguageTags(SettingFragment.languageIn)
            AppCompatDelegate.setApplicationLocales(appLocale)
        } else {
            val appLocale: LocaleListCompat =
                LocaleListCompat.forLanguageTags(SettingFragment.languageEn)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }
}
