package com.sriram.wally.ui.collections

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sriram.wally.models.NetworkResponse
import com.sriram.wally.models.NetworkStatus
import com.sriram.wally.models.response.Collection
import com.sriram.wally.networking.NetworkRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CollectionsViewModel(val networkRepo: NetworkRepo) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val collectionsData = MutableLiveData<NetworkResponse<Collection>>()

    var page = 1

    init {
        refreshData(page)
    }

    private fun refreshData(page: Int) {
        collectionsData.value = NetworkResponse(NetworkStatus.LOADING)
        val disposable = networkRepo.getAllCollections(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it != null && it.isNotEmpty()) {
                        collectionsData.value = NetworkResponse(NetworkStatus.SUCCESS, ArrayList(it))
                    } else {
                        collectionsData.value = NetworkResponse(NetworkStatus.FAILURE)
                    }
                }, {
                    it.printStackTrace()
                    collectionsData.value = NetworkResponse(NetworkStatus.FAILURE)
                })
        compositeDisposable.add(disposable)
    }

    fun getCollections(): LiveData<NetworkResponse<Collection>> {
        return collectionsData
    }

    fun loadMore() {
        val disposable = networkRepo.getAllCollections(++page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it != null && it.isNotEmpty()) {
                        val oldData = collectionsData.value?.items
                        oldData?.addAll(it)
                        collectionsData.value = NetworkResponse(NetworkStatus.SUCCESS, oldData
                                ?: arrayListOf())
                    }
                }, {
                    it.printStackTrace()
                })
        compositeDisposable.addAll(disposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}