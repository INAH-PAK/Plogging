package com.example.plogging.data.model

import com.naver.maps.map.overlay.Marker

sealed class RestRoomMarkersUiState<T> {
    data class Success<T>(val restRoomList: List<Marker>) : RestRoomMarkersUiState<T>()
    data class Error<T>(val message: String) : RestRoomMarkersUiState<T>()
}