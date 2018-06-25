package com.sriram.wally.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileImage(

        @field:SerializedName("small")
        val small: String? = null,

        @field:SerializedName("large")
        val large: String? = null,

        @field:SerializedName("medium")
        val medium: String? = null
) : Parcelable