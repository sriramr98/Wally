package com.sriram.wally.networking

import com.sriram.wally.models.response.*
import com.sriram.wally.models.response.Collection
import com.sriram.wally.utils.SharedPrefUtils
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call

class NetworkRepo(private val wallyService: WallyService) {

    fun getAllPhotos(page: Int, perPage: Int = 10, orderBy: String = SharedPrefUtils.getPhotosOrder()): Single<List<Photo>> {
        return wallyService.getAllPhotos(page, perPage, orderBy)
    }

    fun getDetailsOfASinglePhoto(id: String): Single<PhotoDetailResponse> {
        return wallyService.getDetailOfPhoto(id)
    }

    fun getDownloadsEndpoint(id: String?): Call<DownloadResponse> {
        return wallyService.getDownloadUrl(id!!)
    }

    fun getAllCollections(page: Int): Observable<List<Collection>> {
        return wallyService.getAllCollections(page)
    }

    fun getPhotosOfCollection(id: String, page: Int): Single<List<Photo>> {
        return wallyService.getAllPhotosOfCollection(id, page)
    }

    fun searchPhotos(query: String, page: Int): Single<PhotoSearchResponse> {
        return wallyService.searchPhotos(query, page)
    }

    fun searchCollections(query: String, page: Int): Single<CollectionSearchResponse> {
        return wallyService.searchCollections(query, page)
    }

}