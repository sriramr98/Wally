package com.sriram.wally.ui.home

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

class PhotosListViewModel(val networkRepo: NetworkRepo) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val photosData = MutableLiveData<NetworkResponse<PhotoListResponse>>()
    private var page = -1

    init {
        refresh()
        photosData.value = NetworkResponse(NetworkStatus.LOADING)
    }

    fun refresh() {
        photosData.value?.status = NetworkStatus.LOADING
        page++
        val call = networkRepo.getAllPhotos(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it != null && it.isNotEmpty()) {
                        // success
                        val items = photosData.value?.items
                        items?.addAll(it)
                        photosData.value = NetworkResponse(NetworkStatus.SUCCESS, items
                                ?: arrayListOf())
                    } else {
                        // failure
                        val items = photosData.value?.items
                        items?.addAll(it)
                        photosData.value = NetworkResponse(NetworkStatus.FAILURE, items
                                ?: arrayListOf())
                    }
                }, {
                    val items = photosData.value?.items
                    photosData.value = NetworkResponse(NetworkStatus.FAILURE, items
                            ?: arrayListOf())
                    it.printStackTrace()
                })
        disposable.add(call)
    }

    // to be called when we need to refresh the whole list
    fun fromStart() {
        photosData.value = NetworkResponse(NetworkStatus.LOADING)
        // reset page and reload
        page = 0
        refresh()
    }

    fun getPhotosData(): LiveData<NetworkResponse<PhotoListResponse>> {
        return photosData
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

}