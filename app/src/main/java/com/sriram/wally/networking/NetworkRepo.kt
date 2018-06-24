package com.sriram.wally.networking

import com.sriram.wally.models.response.PhotoListResponse
import com.sriram.wally.utils.SharedPrefUtils
import io.reactivex.Observable

class NetworkRepo(private val wallyService: WallyService) {

    fun getAllPhotos(page: Int, perPage: Int = 10, orderBy: String = SharedPrefUtils.getPhotosOrder()): Observable<List<PhotoListResponse>> {
        return wallyService.getAllPhotos(page, perPage, orderBy)
    }

}