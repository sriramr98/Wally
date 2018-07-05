package com.sriram.wally.models.response

import com.google.gson.annotations.SerializedName

data class PhotoDetailResponse(

        @field:SerializedName("current_user_collections")
        val currentUserCollections: List<CurrentUserCollectionsItem?>? = null,

        @field:SerializedName("color")
        val color: String? = null,

        @field:SerializedName("created_at")
        val createdAt: String? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("liked_by_user")
        val likedByUser: Boolean? = null,

        @field:SerializedName("urls")
        val urls: Urls? = null,

        @field:SerializedName("updated_at")
        val updatedAt: String? = null,

        @field:SerializedName("downloads")
        val downloads: Int? = null,

        @field:SerializedName("width")
        val width: Int? = null,

        @field:SerializedName("location")
        val location: Location? = null,

        @field:SerializedName("links")
        val links: Links? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("user")
        val user: User? = null,

        @field:SerializedName("height")
        val height: Int? = null,

        @field:SerializedName("likes")
        val likes: Int? = null,

        @field:SerializedName("exif")
        val exif: Exif? = null
)