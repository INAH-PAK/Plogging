package com.example.plogging.data.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Documents(
    val documents: List<KakaoApiResponse>
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class KakaoApiResponse(
    val id: String,
    val place_name: String,
    val category_name: String,
    val category_group_code: String,
    val category_group_name: String,
    val phone: String,
    val address_name: String,
    val road_address_name: String,
    val x: String,    //X 좌표값, 경위도인 경우 longitude (경도)
    val y: String,    //Y 좌표값, 경위도인 경우 latitude(위도)
    val place_url: String,
    val distance: String
) : Parcelable