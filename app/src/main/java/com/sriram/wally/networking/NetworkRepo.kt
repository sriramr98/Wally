package com.sriram.wally.networking

import com.sriram.wally.models.response.DownloadResponse
import com.sriram.wally.models.response.PhotoDetailResponse
import com.sriram.wally.models.response.PhotoListResponse
import com.sriram.wally.utils.SharedPrefUtils
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback

class NetworkRepo(private val wallyService: WallyService) {

    fun getAllPhotos(page: Int, perPage: Int = 10, orderBy: String = SharedPrefUtils.getPhotosOrder()): Single<List<PhotoListResponse>> {
        return wallyService.getAllPhotos(page, perPage, orderBy)
    }

    fun getDetailsOfASinglePhoto(id: String): Single<PhotoDetailResponse> {
        return wallyService.getDetailOfPhoto(id)
    }

    fun getDownloadsEndpoint(id: String?): Call<DownloadResponse> {
        return wallyService.getDownloadUrl(id!!)
    }

}