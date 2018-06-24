package com.sriram.wally.utils

import com.sriram.wally.BuildConfig
import timber.log.Timber

object Logger {

    fun i(message: Any) {
        if (BuildConfig.DEBUG) {
            Timber.i(message.toString())
        }
    }

    fun i(throwable: Throwable, message: String = "") {
        if (BuildConfig.DEBUG) {
            if (message.isBlank()) {
                Timber.i(throwable)
            } else {
                Timber.i(throwable, message)
            }
        }
    }

    fun i(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Timber.tag(tag).i(message)
        }
    }

    fun d(message: String) {
        if (BuildConfig.DEBUG) {
            Timber.d(message)
        }
    }

    fun d(throwable: Throwable, message: String = "") {
        if (BuildConfig.DEBUG) {
            if (message.isBlank()) {
                Timber.d(throwable)
            } else {
                Timber.d(throwable, message)
            }
        }
    }

    fun e(message: String) {
        if (BuildConfig.DEBUG) {
            Timber.e(message)
        }
    }

    fun e(throwable: Throwable, message: String = "") {
        if (BuildConfig.DEBUG) {
            if (message.isBlank()) {
                Timber.e(throwable)
            } else {
                Timber.e(throwable, message)
            }
        }
    }

    fun w(message: String) {
        if (BuildConfig.DEBUG) {
            Timber.w(message)
        }
    }

    fun w(throwable: Throwable, message: String = "") {
        if (BuildConfig.DEBUG) {
            if (message.isBlank()) {
                Timber.w(throwable)
            } else {
                Timber.w(throwable, message)
            }
        }
    }

}