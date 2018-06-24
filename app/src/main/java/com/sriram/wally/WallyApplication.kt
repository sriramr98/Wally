package com.sriram.wally

import android.app.Application
import com.sriram.wally.di.modules
import com.sriram.wally.utils.SharedPrefUtils
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class WallyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        SharedPrefUtils.init(this)

        startKoin(this, listOf(modules))


        Timber.plant(Timber.DebugTree())
    }
}