package com.sriram.wally.models.response

import com.google.gson.annotations.SerializedName

data class CollectionSearchResponse(
        @field:SerializedName("total_pages")
        var totalPages: Int? = null,
        @field:SerializedName("results")
        var collections: ArrayList<Collection>? = null
)