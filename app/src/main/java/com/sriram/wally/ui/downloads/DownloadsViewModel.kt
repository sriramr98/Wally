package com.sriram.wally.ui.downloads

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.sriram.wally.db.ImagesRepo
import com.sriram.wally.models.ImageModel

class DownloadsViewModel(val imageRepo: ImagesRepo) : ViewModel() {

    fun getAllDownloadedImages(): LiveData<List<ImageModel>> {
        return imageRepo.getAllDonwloadedImages()
    }

}