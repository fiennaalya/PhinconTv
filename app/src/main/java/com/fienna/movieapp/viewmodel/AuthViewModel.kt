package com.fienna.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import com.fienna.movieapp.core.domain.model.DataUser
import com.fienna.movieapp.core.domain.state.FlowState
import com.fienna.movieapp.core.domain.usecase.MovieUsecase
import com.fienna.movieapp.utils.ValidationHelper.validateEmail
import com.fienna.movieapp.utils.ValidationHelper.validatePassword
import com.fienna.movieapp.utils.ValidationHelper.validateRequired
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

class AuthViewModel(private val movieUsecase: MovieUsecase):ViewModel() {
    private  val _loginEmail = MutableStateFlow<FlowState<Boolean>>(FlowState.FlowCreated)
    val loginEmail = _loginEmail.asStateFlow()

    private  val _loginPass = MutableStateFlow<FlowState<Boolean>>(FlowState.FlowCreated)
    val loginPass = _loginPass.asStateFlow()

    private val _loginValidation: MutableStateFlow<FlowState<Boolean>> = MutableStateFlow(FlowState.FlowCreated)
    val loginValidation = _loginValidation.asSharedFlow()

    private  val _registerEmail = MutableStateFlow<FlowState<Boolean>>(FlowState.FlowCreated)
    val registerEmail = _registerEmail.asStateFlow()

    private  val _registerPass = MutableStateFlow<FlowState<Boolean>>(FlowState.FlowCreated)
    val registerPass = _registerPass.asStateFlow()

    private val _registerValidation: MutableStateFlow<FlowState<Boolean>> = MutableStateFlow(FlowState.FlowCreated)
    val registerValidation = _registerValidation.asSharedFlow()

    private  val _profileNameValidation = MutableStateFlow<FlowState<Boolean>>(FlowState.FlowCreated)
    val profileNameValidation = _profileNameValidation.asStateFlow()

    fun signUp(email: String, password: String): Flow<Boolean> = runBlocking {
        movieUsecase.signUp(email, password)
    }

    fun signIn(email: String, password: String): Flow<Boolean> = runBlocking {
        movieUsecase.signIn(email, password)
    }

    fun getCurrentUser(): DataUser? {
        return movieUsecase.getCurrentUser()
    }

    /*email - login validation*/
    fun updateProfile(userProfileChangeRequest: UserProfileChangeRequest): Flow<Boolean> = runBlocking {
        movieUsecase.updateProfile(userProfileChangeRequest)
    }

    fun validateLoginEmail(email:String){
        _loginEmail.update { FlowState.FlowValue(email.validateEmail() )}
    }

    fun validateLoginPass(password:String){
        _loginPass.update { FlowState.FlowValue(password.validatePassword())}
    }

    fun validateLoginField(email: String, password: String) {
        _loginValidation.update { FlowState.FlowValue(email.validateRequired() && password.validateRequired()) }
    }

    fun resetLoginValidationState() {
        _loginValidation.value = FlowState.FlowCreated
    }

    fun validateRegisterEmail(email:String){
        _registerEmail.update { FlowState.FlowValue(email.validateEmail())}
    }

    fun validateRegisterPass(password:String){
        _registerPass.update { FlowState.FlowValue(password.validatePassword())}
    }

    fun validateRegisterField(email: String, password: String){
        _registerValidation.update {FlowState.FlowValue(email.validateRequired() && password.validateRequired())}
    }

    fun resetRegisterValidationState() {
        _registerValidation.value = FlowState.FlowCreated
    }

    fun validateProfileField(name:String){
        _profileNameValidation.update { FlowState.FlowValue(name.validateRequired())}
    }

    fun resetProfileValidationState() {
        _profileNameValidation.value = FlowState.FlowCreated
    }

    fun saveProfileName(name: String){
        movieUsecase.putProfileName(name)
    }

    fun saveUserId(id:String){
        movieUsecase.putUserId(id)
    }


}