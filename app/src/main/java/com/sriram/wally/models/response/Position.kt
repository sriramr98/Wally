package com.sriram.wally.models.response

import com.google.gson.annotations.SerializedName

data class Position(

        @field:SerializedName("latitude")
        val latitude: Double? = null,

        @field:SerializedName("longitude")
        val longitude: Double? = null
)