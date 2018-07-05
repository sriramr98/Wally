package com.sriram.wally.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(

        @field:SerializedName("updated_at")
        val updatedAt: String? = null,

        @field:SerializedName("total_photos")
        val totalPhotos: Int? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("bio")
        val bio: String? = null,

        @field:SerializedName("location")
        val location: String? = null,

        @field:SerializedName("total_collections")
        val totalCollections: Int? = null,

        @field:SerializedName("links")
        val links: Links? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("total_likes")
        val totalLikes: Int? = null,

        @field:SerializedName("portfolio_url")
        val portfolioUrl: String? = null,

        @field:SerializedName("username")
        val username: String? = null
) : Parcelable