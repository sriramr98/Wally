package com.sriram.wally.models.response

import com.google.gson.annotations.SerializedName

data class User(

        @field:SerializedName("total_photos")
        val totalPhotos: Int? = null,

        @field:SerializedName("twitter_username")
        val twitterUsername: String? = null,

        @field:SerializedName("bio")
        val bio: String? = null,

        @field:SerializedName("total_likes")
        val totalLikes: Int? = null,

        @field:SerializedName("portfolio_url")
        val portfolioUrl: String? = null,

        @field:SerializedName("profile_image")
        val profileImage: ProfileImage? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("location")
        val location: String? = null,

        @field:SerializedName("total_collections")
        val totalCollections: Int? = null,

        @field:SerializedName("links")
        val links: Links? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("instagram_username")
        val instagramUsername: String? = null,

        @field:SerializedName("username")
        val username: String? = null
)