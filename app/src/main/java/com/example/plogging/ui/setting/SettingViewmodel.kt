package com.example.plogging.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.plogging.data.source.PreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingViewmodel(private val preferenceRepository: PreferenceRepository) : ViewModel() {

    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    private val _photo = MutableLiveData("")
    val photo: LiveData<String> = _photo

    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _isLogin = MutableStateFlow<Boolean>(true)
    val isLogin = _isLogin.asStateFlow()

    init {
        viewModelScope.launch {
            getUserPreference()
        }
    }

    private suspend fun getUserPreference() {
        _name.value = preferenceRepository.getUserName()
        _photo.value = preferenceRepository.getUserProfileUrl()
        _email.value = preferenceRepository.getUserEmail()
    }

    fun logout() {
        viewModelScope.launch {
            preferenceRepository.logout()
            _isLogin.emit(false)
        }
    }

    companion object {
        fun provideFactory(preferenceRepository: PreferenceRepository) = viewModelFactory {
            initializer {
                SettingViewmodel(preferenceRepository)
            }
        }
    }
}