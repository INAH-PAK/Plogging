package com.example.plogging.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "local_recodes")
data class AloneRecode(
    val createDate: String,
    val thumbnailImageString: String,
    @ColumnInfo(name = "tracking_time") val trackingTime: String,
    @ColumnInfo(name = "tracking_distance") val trackingDistance: String,
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @IgnoredOnParcel
    val id: Int = 0
}