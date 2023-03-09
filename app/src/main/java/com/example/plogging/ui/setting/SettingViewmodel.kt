package com.example.plogging.ui.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.plogging.data.source.UserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch

class SettingViewmodel(private val preferenceRepository: UserDataRepository) : ViewModel() {

    val name: StateFlow<String> =
        preferenceRepository.getUserName().stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(3000),
            initialValue = "null"
        )

    val photo: StateFlow<String> =
        preferenceRepository.getUserProfileUrl().stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(3000),
            initialValue = "null"
        )

    val email: StateFlow<String> =
        preferenceRepository.getUserEmail().stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(3000),
            initialValue = "null"
        )

    private val _isLogin = MutableStateFlow<Boolean>(true)
    val isLogin = _isLogin.asStateFlow()

    private val _clickUserName = MutableSharedFlow<Boolean>(replay = 0)
    val clickUserName = _clickUserName.asSharedFlow()

    private val _clickDialogButton = MutableStateFlow<Boolean>(false)
    val clickDialogButton = _clickDialogButton.asStateFlow()

    private val _clickUserProfileImage = MutableSharedFlow<Boolean>(replay = 0)
    val clickUserProfileImage = _clickUserProfileImage.asSharedFlow()

    fun onclickUserProfileImage() {
        // 갤러리 에서 사진 선택 기능 추가
        // 1. 사진 퍼미션을 받는다
        // 2. 한 장만 사진을 선택받아 이미지 추가.
        viewModelScope.launch(Dispatchers.Main) {
            Log.i("rr", "뷰모델")
            _clickUserProfileImage.emit(true)
        }
    }

    suspend fun onSelectGalleryImage(image: String) {
        preferenceRepository.changeUserProfileImage(image)
    }

    fun onClickUserProfileName() {
        viewModelScope.launch {
            _clickUserName.emit(true)
        }
    }

    fun onClickDialogComplete(text: String) {
        viewModelScope.launch {
            Log.i("사용자 입력 닉네임 ", text)
            preferenceRepository.changeUserName(text)
            _clickDialogButton.emit(true)
        }
    }

    fun onClickDialogCancel() {
        viewModelScope.launch {
            _clickDialogButton.emit(true)
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferenceRepository.logout()
            _isLogin.emit(false)
        }
    }

    companion object {
        fun provideFactory(preferenceRepository: UserDataRepository) = viewModelFactory {
            initializer {
                SettingViewmodel(preferenceRepository)
            }
        }
    }
}