package com.sriram.wally.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "downloaded-images")
@Parcelize
data class ImageModel(
        @PrimaryKey(autoGenerate = false)
        var id: String,
        var imagePath: String
) : Parcelable