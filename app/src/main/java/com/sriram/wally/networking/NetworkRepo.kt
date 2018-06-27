package com.sriram.wally.networking

import com.sriram.wally.models.response.PhotoListResponse
import com.sriram.wally.utils.SharedPrefUtils
import io.reactivex.Observable
import io.reactivex.Single

class NetworkRepo(private val wallyService: WallyService) {

    fun getAllPhotos(page: Int, perPage: Int = 10, orderBy: String = SharedPrefUtils.getPhotosOrder()): Single<List<PhotoListResponse>> {
        return wallyService.getAllPhotos(page, perPage, orderBy)
    }

}