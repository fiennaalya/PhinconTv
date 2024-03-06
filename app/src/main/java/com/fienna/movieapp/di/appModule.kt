package com.fienna.movieapp.di

import com.fienna.movieapp.viewmodel.AuthViewModel
import com.fienna.movieapp.viewmodel.CartViewModel
import com.fienna.movieapp.viewmodel.DashboardViewModel
import com.fienna.movieapp.viewmodel.DetailViewModel
import com.fienna.movieapp.viewmodel.FirebaseViewModel
import com.fienna.movieapp.viewmodel.HomeViewModel
import com.fienna.movieapp.viewmodel.PreLoginViewModel
import com.fienna.movieapp.viewmodel.SearchViewModel
import com.fienna.movieapp.viewmodel.TokenViewModel
import com.fienna.movieapp.viewmodel.WishlistViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val firebaseModule = module {
    single { FirebaseAnalytics.getInstance(androidContext()) }
}

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { PreLoginViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { WishlistViewModel(get(), get()) }
    viewModel { CartViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { TokenViewModel(get()) }
    viewModel { FirebaseViewModel(get()) }
}

val appModule = module {
    includes(viewModelModule, firebaseModule)
}
