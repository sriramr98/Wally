package com.sriram.wally.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Links(

        @field:SerializedName("portfolio")
        val portfolio: String? = null,

        @field:SerializedName("self")
        val self: String? = null,

        @field:SerializedName("html")
        val html: String? = null,

        @field:SerializedName("photos")
        val photos: String? = null,

        @field:SerializedName("likes")
        val likes: String? = null,

        @field: SerializedName("download")
        val download: String? = null,

        @field: SerializedName("download_location")
        val downloadLocation: String? = null
) : Parcelable