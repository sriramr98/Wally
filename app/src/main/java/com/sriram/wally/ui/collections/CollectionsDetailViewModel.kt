package com.sriram.wally.ui.collections

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sriram.wally.models.NetworkResponse
import com.sriram.wally.models.NetworkStatus
import com.sriram.wally.models.response.PhotoListResponse
import com.sriram.wally.networking.NetworkRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CollectionsDetailViewModel(private val wallyRepo: NetworkRepo) : ViewModel() {

    private val photosData = MutableLiveData<NetworkResponse<PhotoListResponse>>()
    private val compositeDisposable = CompositeDisposable()
    private var id: String = ""

    private fun queryData(id: String = this.id, page: Int = 1) {
        photosData.value = NetworkResponse(NetworkStatus.LOADING)
        val disposable = wallyRepo.getPhotosOfCollection(id, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val oldData = photosData.value?.items
                    if (oldData != null) {
                        oldData.addAll(it)
                        photosData.value = NetworkResponse(NetworkStatus.SUCCESS, oldData)
                    } else {
                        photosData.value = NetworkResponse(NetworkStatus.FAILURE)
                    }
                }, {
                    photosData.value = NetworkResponse(NetworkStatus.FAILURE)
                })
        compositeDisposable.add(disposable)
    }

    fun getPhotos(id: String): LiveData<NetworkResponse<PhotoListResponse>> {
        if (photosData.value == null) {
            this.id = id
            queryData()
        }
        return photosData
    }

}