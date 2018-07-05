package com.sriram.wally.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.sriram.wally.models.ImageModel

@Database(entities = [(ImageModel::class)], version = 1, exportSchema = true)
abstract class WallyDatabase : RoomDatabase() {

    abstract val downloadedImagesDao: DownloadedImagesDao

    companion object {
        fun getWallyDatabase(context: Context): WallyDatabase {
            return Room.databaseBuilder(context,
                    WallyDatabase::class.java, "database-name").allowMainThreadQueries().build()
        }
    }
}