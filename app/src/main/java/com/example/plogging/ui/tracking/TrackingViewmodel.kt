package com.example.plogging.ui.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.plogging.data.model.RestRoomMarkersUiState
import com.example.plogging.data.model.TogetherRecode
import com.example.plogging.data.model.User
import com.example.plogging.data.source.TrackingRepository
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TrackingViewModel(private val repository: TrackingRepository) : ViewModel() {

    private val _restroomItemUiState =
        MutableStateFlow(RestRoomMarkersUiState.Success<List<Marker>>(emptyList()))

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<RestRoomMarkersUiState<List<Marker>>> = _restroomItemUiState

    fun postRecode() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.postTogetherRecodes(
                "2022-03-19",
                TogetherRecode(
                    arrayListOf(
                        User("사용자 토큰1", "인아", "ㅏㅏㅏ"),
                        User("사용자 토큰2", "인아2", "ㅏ22"),
                    ),
                    ",",
                    300L,
                    130L
                )
            )
        }
    }

    fun onClickChipRestroom() {
        viewModelScope.launch {
            repository.getKakaoLocalApi(
                "37.514322572335935",
                "127.06283102249932",
                20000,
            ).map { result ->
                result.documents.map {
                    Marker().apply {
                        this.position = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
                        this.captionText = it.place_name
                    }
                }
            }.catch { e ->
                RestRoomMarkersUiState.Error<List<Marker>>("키워드 데이터를 받아올 수 없습니다.")
            }.collect { makers ->
                _restroomItemUiState.update {
                    RestRoomMarkersUiState.Success<List<Marker>>(makers)
                }
            }

        }
    }

    companion object {
        fun provideFactory(trackingRepository: TrackingRepository) = viewModelFactory {
            initializer {
                TrackingViewModel(trackingRepository)
            }
        }
    }
}