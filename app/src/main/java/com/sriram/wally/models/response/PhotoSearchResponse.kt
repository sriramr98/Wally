package com.sriram.wally.models.response

import com.google.gson.annotations.SerializedName

data class PhotoSearchResponse(
        @field:SerializedName("total_pages")
        var totalPages: Int? = null,
        @field:SerializedName("results")
        var photos: ArrayList<Photo>? = null
)