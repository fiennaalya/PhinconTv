package com.fienna.movieapp.core.di

import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.fienna.movieapp.core.data.local.database.MovieDatabase
import com.fienna.movieapp.core.data.local.datasource.LocalDataSource
import com.fienna.movieapp.core.data.local.preferences.SharedPref
import com.fienna.movieapp.core.data.local.preferences.SharedPrefImpl
import com.fienna.movieapp.core.data.remote.client.MovieClient
import com.fienna.movieapp.core.data.remote.datasource.PagingDataSourceImpl
import com.fienna.movieapp.core.data.remote.datasource.RemoteDataSource
import com.fienna.movieapp.core.data.remote.interceptor.MovieInterceptor
import com.fienna.movieapp.core.data.remote.service.ApiEndPoint
import com.fienna.movieapp.core.domain.repository.FirebaseRepository
import com.fienna.movieapp.core.domain.repository.FirebaseRepositoryImpl
import com.fienna.movieapp.core.domain.repository.PreLoginRepository
import com.fienna.movieapp.core.domain.repository.PreLoginRepositoryImpl
import com.fienna.movieapp.core.domain.repository.RemoteRepository
import com.fienna.movieapp.core.domain.repository.RemoteRepositoryImpl
import com.fienna.movieapp.core.domain.repository.RoomRepository
import com.fienna.movieapp.core.domain.repository.RoomRepositoryImpl
import com.fienna.movieapp.core.domain.usecase.MovieInteractor
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.auth
import com.google.firebase.remoteconfig.remoteConfig
import org.koin.android.ext.koin.androidContext
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
    single { LocalDataSource(get(), get()) }
    single { RemoteDataSource(get()) }
    single { PagingDataSourceImpl(get()) }
}

val repositoryModules = module {
    single<FirebaseRepository> { FirebaseRepositoryImpl(get(),get(),get()) }
    single<PreLoginRepository> { PreLoginRepositoryImpl(get()) }
    single<RemoteRepository> { RemoteRepositoryImpl(get(),get()) }
    single<RoomRepository> { RoomRepositoryImpl(get()) }
}

val roomDatabaseModules = module {
    single {
        Room.databaseBuilder(androidContext(), MovieDatabase::class.java, "movie_database")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<MovieDatabase>().movieDao() }
}

val networkModules = module {
    single { MovieInterceptor() }
    single { ChuckerInterceptor.Builder(androidContext()).redactHeaders("Authorization", "Bearer").build() }
    single { MovieClient(get(), get()) }
    single<ApiEndPoint>{get<MovieClient>().create()}
}

val usecaseModules = module {
    single<MovieUsecase> { MovieInteractor(get(),get(), get(), get()) }
}

val coreModule = module {
    includes(databaseModules, sharedPrefModules, repositoryModules, firebaseModule, usecaseModules, networkModules, databaseModules, roomDatabaseModules)
}
