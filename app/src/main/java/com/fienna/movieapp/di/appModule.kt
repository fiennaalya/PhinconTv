package com.fienna.movieapp.di

import com.fienna.movieapp.viewmodel.AuthViewModel
import com.fienna.movieapp.viewmodel.DashboardViewModel
import com.fienna.movieapp.viewmodel.HomeViewModel
import com.fienna.movieapp.viewmodel.PreLoginViewModel
import com.fienna.movieapp.viewmodel.WishlistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{AuthViewModel(get())}
    viewModel{PreLoginViewModel(get()) }
    viewModel{DashboardViewModel(get())}
    viewModel{HomeViewModel(get())}
    viewModel{WishlistViewModel(get())}
}

val appModule = module {
    includes(viewModelModule)
}