package com.sriram.wally.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoListResponse(

        @field:SerializedName("color")
        val color: String? = null,

        @field:SerializedName("created_at")
        val createdAt: String? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("urls")
        val urls: Urls? = null,

        @field:SerializedName("updated_at")
        val updatedAt: String? = null,

        @field:SerializedName("width")
        val width: Int? = null,

        @field:SerializedName("links")
        val links: Links? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("user")
        val user: User? = null,

        @field:SerializedName("height")
        val height: Int? = null,

        @field:SerializedName("likes")
        val likes: Int? = null
): Parcelable