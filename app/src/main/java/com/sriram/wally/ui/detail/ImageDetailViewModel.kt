package com.sriram.wally.ui.detail

import android.arch.lifecycle.ViewModel
import com.sriram.wally.models.response.PhotoDetailResponse
import com.sriram.wally.networking.NetworkRepo
import com.sriram.wally.utils.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ImageDetailViewModel(private val networkRepo: NetworkRepo) : ViewModel() {

    lateinit var imageData: PhotoDetailResponse
    private val compositeDisposable = CompositeDisposable()

    fun loadData(id: String) {
        val disposable = networkRepo.getDetailsOfASinglePhoto(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    imageData = it
                    Logger.i(it)
                }, {
                    it.printStackTrace()
                })
        compositeDisposable.add(disposable)
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun getId(): String? {
        return imageData.id
    }
}