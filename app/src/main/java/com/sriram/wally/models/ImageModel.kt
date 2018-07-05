package com.sriram.wally.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "downloaded-images")
data class ImageModel(
        @PrimaryKey(autoGenerate = false)
        var id: String,
        var imagePath: String
)