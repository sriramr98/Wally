package com.sriram.wally.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(

        @field:SerializedName("country")
        val country: String? = null,

        @field:SerializedName("city")
        val city: String? = null,

        @field:SerializedName("position")
        val position: Position? = null
): Parcelable