package com.fienna.movieapp.core.di

import com.fienna.movieapp.core.data.local.datasource.LocalDataSource
import com.fienna.movieapp.core.data.local.preferences.SharedPref
import com.fienna.movieapp.core.data.local.preferences.SharedPrefImpl
import com.fienna.movieapp.core.domain.repository.FirebaseRepository
import com.fienna.movieapp.core.domain.repository.FirebaseRepositoryImpl
import com.fienna.movieapp.core.domain.repository.PreLoginRepository
import com.fienna.movieapp.core.domain.repository.PreLoginRepositoryImpl
import com.fienna.movieapp.core.domain.usecase.MovieInteractor
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.auth
import com.google.firebase.remoteconfig.remoteConfig
import org.koin.dsl.module

val firebaseModule = module {
    single { Firebase.auth }
    single { Firebase.analytics }
    single { Firebase.remoteConfig }
}

val sharedPrefModules = module {
    single <SharedPref>{SharedPrefImpl(get()) }
}

val databaseModules = module {
    single { LocalDataSource(get()) }
}

val repositoryModules = module {
    single<FirebaseRepository> { FirebaseRepositoryImpl(get()) }
    single<PreLoginRepository> { PreLoginRepositoryImpl(get()) }

}

val usecaseModules = module {
    single<MovieUsecase> { MovieInteractor(get(),get()) }
}

val coreModule = module {
    includes(databaseModules, sharedPrefModules, repositoryModules, firebaseModule, usecaseModules)
}