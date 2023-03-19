package com.example.plogging.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "local_recodes")
data class AloneRecode constructor(
    @ColumnInfo(name = "create_date") val createDate: String,
    val thumbnailImageString: String,
    @ColumnInfo(name = "tracking_time") val trackingTime: String,
    @ColumnInfo(name = "tracking_distance") val trackingDistance: String,
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @IgnoredOnParcel
    var id: Long = 0

    constructor(createDate: String,thumbnailImageString: String, trackingTime: String, trackingDistance: String, id:Long)
            : this(createDate,thumbnailImageString, trackingTime,trackingDistance) {
        this.id = id
    }
}