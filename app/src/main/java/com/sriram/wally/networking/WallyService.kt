package com.sriram.wally.networking

import com.sriram.wally.models.response.*
import com.sriram.wally.models.response.Collection
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WallyService {

    @GET("photos")
    fun getAllPhotos(@Query("page") page: Int, @Query("per_page") perPage: Int, @Query("order_by") order: String): Single<List<Photo>>

    @GET("photos/{id}")
    fun getDetailOfPhoto(@Path("id") id: String): Single<PhotoDetailResponse>

    @GET("photos/{id}/download")
    fun getDownloadUrl(@Path("id") id: String): Call<DownloadResponse>

    @GET("/collections")
    fun getAllCollections(@Query("page") page: Int): Observable<List<Collection>>

    @GET("/collections/{id}/photos")
    fun getAllPhotosOfCollection(@Path("id") id: String, @Query("page") page: Int): Single<List<Photo>>

    @GET("/search/photos")
    fun searchPhotos(@Query("query") query: String, @Query("page") page: Int = 1): Single<PhotoSearchResponse>

    @GET("/search/collections")
    fun searchCollections(@Query("query") query: String, @Query("page") page: Int = 1): Single<CollectionSearchResponse>

    @GET("/photos/random")
    fun getRandomImage(): Call<Photo>
}