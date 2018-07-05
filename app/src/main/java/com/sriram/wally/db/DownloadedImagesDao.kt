package com.sriram.wally.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sriram.wally.models.ImageModel

@Dao
interface DownloadedImagesDao {

    @Query("SELECT * FROM `downloaded-images`")
    fun getAllImages(): LiveData<List<ImageModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(image: ImageModel)

}