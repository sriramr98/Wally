package com.sriram.wally.models.response

import com.google.gson.annotations.SerializedName

data class Location(

        @field:SerializedName("country")
        val country: String? = null,

        @field:SerializedName("city")
        val city: String? = null,

        @field:SerializedName("position")
        val position: Position? = null
)