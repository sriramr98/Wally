package com.sriram.wally.networking

import com.sriram.wally.models.response.DownloadResponse
import com.sriram.wally.models.response.PhotoDetailResponse
import com.sriram.wally.models.response.PhotoListResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WallyService {

    @GET("photos")
    fun getAllPhotos(@Query("page") page: Int, @Query("per_page") perPage: Int, @Query("order_by") order: String): Single<List<PhotoListResponse>>

    @GET("photos/{id}")
    fun getDetailOfPhoto(@Path("id") id: String): Single<PhotoDetailResponse>

    @GET("photos/{id}/download")
    fun getDownloadUrl(@Path("id") id: String): Call<DownloadResponse>

}