package com.sriram.wally.models.response

import com.google.gson.annotations.SerializedName

data class CurrentUserCollectionsItem(

        @field:SerializedName("cover_photo")
        val coverPhoto: Any? = null,

        @field:SerializedName("updated_at")
        val updatedAt: String? = null,

        @field:SerializedName("curated")
        val curated: Boolean? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("published_at")
        val publishedAt: String? = null,

        @field:SerializedName("user")
        val user: Any? = null
)