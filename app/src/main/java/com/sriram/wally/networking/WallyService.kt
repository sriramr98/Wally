package com.sriram.wally.networking

import com.sriram.wally.models.response.PhotoListResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WallyService {

    @GET("photos")
    fun getAllPhotos(@Query("page") page: Int, @Query("per_page") perPage: Int, @Query("order_by") order: String): Observable<List<PhotoListResponse>>

}