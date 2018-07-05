package com.sriram.wally.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Exif(

        @field:SerializedName("exposure_time")
        val exposureTime: String? = null,

        @field:SerializedName("aperture")
        val aperture: String? = null,

        @field:SerializedName("focal_length")
        val focalLength: String? = null,

        @field:SerializedName("iso")
        val iso: Int? = null,

        @field:SerializedName("model")
        val model: String? = null,

        @field:SerializedName("make")
        val make: String? = null
) : Parcelable