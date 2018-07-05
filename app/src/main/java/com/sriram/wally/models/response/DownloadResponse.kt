package com.sriram.wally.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DownloadResponse(
        @Expose @SerializedName("url") var url: String
)