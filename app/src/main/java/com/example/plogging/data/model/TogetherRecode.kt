package com.example.plogging.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class TogetherRecode(
    @Json(name = "user_list") val users: List<User>,
    @Json(name = "thumbnail_image_string") val thumbnailImageString: String,
    @Json(name = "tracking_time") val trackingTime: Long,
    @Json(name = "tracking_distance") val trackingDistance: Long
) : Parcelable

@Parcelize
class User(
    @Json(name = "user_id_token") val userIdToken: String,
    @Json(name = "user_name") val userName: String,
    @Json(name = "user_profile") val profileImage: String,
) : Parcelable