package com.sriram.wally.db

import android.arch.lifecycle.LiveData
import com.sriram.wally.models.ImageModel

class ImagesRepo(private val wallyDatabase: WallyDatabase) {

    fun insertImage(image: ImageModel) {
        wallyDatabase.downloadedImagesDao.insertImage(image)
    }

    fun getAllDonwloadedImages(): LiveData<List<ImageModel>> {
        return wallyDatabase.downloadedImagesDao.getAllImages()
    }

}