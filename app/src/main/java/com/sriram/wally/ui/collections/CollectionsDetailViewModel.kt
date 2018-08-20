package com.sriram.wally.ui.collections

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sriram.wally.models.NetworkResponse
import com.sriram.wally.models.NetworkStatus
import com.sriram.wally.models.response.Photo
import com.sriram.wally.networking.NetworkRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CollectionsDetailViewModel(private val wallyRepo: NetworkRepo) : ViewModel() {

    private val photosData = MutableLiveData<NetworkResponse<Photo>>()
    private val compositeDisposable = CompositeDisposable()
    private var id: String = ""
    private var page = 1

    private fun queryData(id: String = this.id) {

        photosData.value = NetworkResponse(NetworkStatus.LOADING)
        val disposable = wallyRepo.getPhotosOfCollection(id, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it != null && it.isNotEmpty()) {
                        photosData.value = NetworkResponse(NetworkStatus.SUCCESS, ArrayList(it))
                    } else {
                        photosData.value = NetworkResponse(NetworkStatus.FAILURE)
                    }
                }, {
                    photosData.value = NetworkResponse(NetworkStatus.FAILURE)
                })
        compositeDisposable.add(disposable)
    }

    fun getPhotos(id: String): LiveData<NetworkResponse<Photo>> {
        if (photosData.value == null) {
            this.id = id
            queryData()
        }
        return photosData
    }

    fun refresh() {
        val disposable = wallyRepo.getPhotosOfCollection(id, ++page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val oldData = photosData.value?.items
                    oldData?.addAll(it)
                    if (it != null && it.isNotEmpty()) {
                        photosData.value = NetworkResponse(NetworkStatus.SUCCESS, oldData
                                ?: arrayListOf())
                    } else {
                        photosData.value = NetworkResponse(NetworkStatus.FAILURE, oldData
                                ?: arrayListOf())
                    }
                }, {
                    photosData.value = NetworkResponse(NetworkStatus.FAILURE, photosData.value?.items
                            ?: arrayListOf())
                })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}