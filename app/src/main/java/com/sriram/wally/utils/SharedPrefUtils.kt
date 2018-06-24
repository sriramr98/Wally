package com.sriram.wally.utils

import android.content.Context
import android.content.SharedPreferences
import org.jetbrains.anko.defaultSharedPreferences

object SharedPrefUtils {

    private lateinit var mPrefs: SharedPreferences

    private const val PHOTOS_ORDER = "photos-order"

    fun init(context: Context) {
        mPrefs = context.defaultSharedPreferences
    }

    fun getPhotosOrder(): String {
        return mPrefs.getString(PHOTOS_ORDER, Constants.PHOTOS_ORDER_LATEST)
    }
}