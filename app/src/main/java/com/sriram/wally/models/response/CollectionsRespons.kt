package com.sriram.wally.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Collection(
        @field:SerializedName("id")
        var id: Int,
        @field:SerializedName("title")
        var title: String,
        @field:SerializedName("description")
        var description: String,
        @field:SerializedName("published_at")
        var publishedAt: String,
        @field:SerializedName("updated_at")
        var updatedAt: String,
        @field:SerializedName("curated")
        var curated: Boolean,
        @field:SerializedName("total_photos")
        var totalPhotos: Int,
        @field:SerializedName("private")
        var private: Boolean,
        @field:SerializedName("cover_photo")
        var coverPhoto: CoverPhoto,
        @field:SerializedName("user")
        var user: User
) : Parcelable

@Parcelize
data class CoverPhoto(
        @field:SerializedName("id")
        var id: String,
        @field:SerializedName("width")
        var width: Int,
        @field:SerializedName("height")
        var height: Int,
        @field:SerializedName("color")
        var color: String,
        @field:SerializedName("description")
        var description: String,
        @field:SerializedName("user")
        var user: User,
        @field:SerializedName("urls")
        var urls: Urls
) : Parcelable
