package com.sriram.wally.utils

import android.content.Context
import android.content.SharedPreferences
import com.sriram.wally.R
import org.jetbrains.anko.defaultSharedPreferences

object SharedPrefUtils {

    private lateinit var mPrefs: SharedPreferences

    private const val PHOTOS_ORDER = "photos-order"
    private const val KEY_RANDOM_WALLPAPER_SCHEDULED = "key-random-wallpaper-scheduled"

    fun init(context: Context) {
        mPrefs = context.defaultSharedPreferences
    }

    fun getPhotosOrder(): String {
        return mPrefs.getString(PHOTOS_ORDER, Constants.PHOTOS_ORDER_LATEST)
    }

    fun getIsRandomWallpaperScheduled(context: Context): Boolean {
        return mPrefs.getBoolean(context.getString(R.string.key_regular_wallpaper), false)
    }

}