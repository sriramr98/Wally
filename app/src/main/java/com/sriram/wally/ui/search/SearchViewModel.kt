package com.sriram.wally.ui.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sriram.wally.models.NetworkResponse
import com.sriram.wally.models.NetworkStatus
import com.sriram.wally.models.response.Collection
import com.sriram.wally.models.response.Photo
import com.sriram.wally.networking.NetworkRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchViewModel(private val networkRepo: NetworkRepo) : ViewModel() {

    private var query: String = ""
    private var searchType: String = SearchActivity.SEARCH_PHOTOS

    private val imagesData = MutableLiveData<NetworkResponse<Photo>>()
    private val collectionsData = MutableLiveData<NetworkResponse<Collection>>()

    init {
        imagesData.value = NetworkResponse(NetworkStatus.LOADING)
        collectionsData.value = NetworkResponse(NetworkStatus.LOADING)
    }

    private var page: Int = 0

    fun setQueryAndType(query: String, searchType: String) {
        this.query = query
        this.searchType = searchType
        refreshData()
    }

    fun refreshData() {
        ++page
        if (searchType == SearchActivity.SEARCH_PHOTOS) {
            searchPhotos()
        } else {
            searchCollection()
        }
    }

    fun resetData() {
        page = 0
        if (searchType == SearchActivity.SEARCH_PHOTOS) {
            imagesData.value = NetworkResponse(NetworkStatus.LOADING)
        } else {
            collectionsData.value = NetworkResponse(NetworkStatus.LOADING)
        }
        refreshData()
    }

    private fun searchCollection() {
        networkRepo.searchCollections(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it?.collections != null && it.collections?.isNotEmpty() == true) {
                        val currentItems = collectionsData.value?.items
                        currentItems?.addAll(it.collections ?: arrayListOf())
                        collectionsData.value = NetworkResponse(NetworkStatus.SUCCESS, currentItems
                                ?: arrayListOf())
                    } else {
                        val items = collectionsData.value?.items
                        collectionsData.value = NetworkResponse(NetworkStatus.FAILURE, items
                                ?: arrayListOf())
                    }
                }, {
                    val items = collectionsData.value?.items
                    collectionsData.value = NetworkResponse(NetworkStatus.FAILURE, items
                            ?: arrayListOf())
                    it.printStackTrace()
                })
    }

    private fun searchPhotos() {
        networkRepo.searchPhotos(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it?.photos != null && it.photos?.isNotEmpty() == true) {
                        val currentItems = imagesData.value?.items
                        currentItems?.addAll(it.photos ?: arrayListOf())
                        imagesData.value = NetworkResponse(NetworkStatus.SUCCESS, currentItems
                                ?: arrayListOf())
                    } else {
                        val items = imagesData.value?.items
                        imagesData.value = NetworkResponse(NetworkStatus.FAILURE, items
                                ?: arrayListOf())
                    }
                }, {
                    val items = imagesData.value?.items
                    imagesData.value = NetworkResponse(NetworkStatus.FAILURE, items
                            ?: arrayListOf())
                    it.printStackTrace()
                })
    }

    fun getImages(): LiveData<NetworkResponse<Photo>> = imagesData

    fun getCollections(): LiveData<NetworkResponse<Collection>> = collectionsData

}