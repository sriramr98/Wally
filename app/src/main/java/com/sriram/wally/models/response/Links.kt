package com.sriram.wally.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Links(

        @field:SerializedName("followers")
        val followers: String? = null,

        @field:SerializedName("portfolio")
        val portfolio: String? = null,

        @field:SerializedName("following")
        val following: String? = null,

        @field:SerializedName("self")
        val self: String? = null,

        @field:SerializedName("html")
        val html: String? = null,

        @field:SerializedName("photos")
        val photos: String? = null,

        @field:SerializedName("likes")
        val likes: String? = null
): Parcelable