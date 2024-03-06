package com.fienna.movieapp.viewmodel

import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import com.fienna.movieapp.utils.MainCoroutineRule
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {
    @get:Rule
    val mainDispatchersRule = MainCoroutineRule()

    @Mock
    lateinit var movieUsecase: MovieUsecase
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = AuthViewModel(movieUsecase)
    }

    @Test
    fun testSignUpWithFirebase_Success() = runTest {
        val email = "rara@gmail.com"
        val password = "12345678"
        val response = flowOf(true)
        `when`(movieUsecase.signUp(email, password)).thenReturn(response)

        val resultTest = viewModel.signUp(email, password)
        val result = mutableListOf<Boolean>()
        resultTest.collect {
            result.add(it)
        }

        verify(movieUsecase).signUp(email, password)
        assert(result.last())
    }

    @Test
    fun testSignUpWithFirebase_Failure() = runTest {
        val email = "lala@gmail.com"
        val password = "12345678"
        val response = flowOf(false)
        `when`(movieUsecase.signUp(email, password)).thenReturn(response)

        val resultTest = viewModel.signUp(email, password)
        val result = mutableListOf<Boolean>()
        resultTest.collect {
            result.add(it)
        }

        verify(movieUsecase).signUp(email, password)
        assert(!result.last())
    }


}
