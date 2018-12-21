package com.sriram.wally.core

import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.sriram.wally.utils.Constants
import java.util.*
import java.util.concurrent.TimeUnit

class WallpaperService : IntentService("WallpaperService") {

    override fun onHandleIntent(intent: Intent?) {
        intent?.apply {
            when (intent.action) {
                ACTION_SETUP_WORKER -> {
                    setupWorker()
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        // Define service as sticky so that it stays in background
        return Service.START_STICKY
    }

    private fun setupWorker() {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis

        // for removing from recent apps test
        // calendar.add(Calendar.SECOND, 10)

        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 10)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
//        calendar.add(Calendar.DAY_OF_MONTH, 1)

        val tomorrowTime = calendar.timeInMillis
        val timeDiffBetweenNowAndTomorrow = tomorrowTime - currentTime

        Log.i("WallpaperService", "************  Tomorrow date is ${calendar.timeInMillis}")
        Log.i("WallpaperService", "************  Difference between now and tomorrow $timeDiffBetweenNowAndTomorrow")

        val randomWorkRequest = OneTimeWorkRequestBuilder<RandomWallpaperWorker>()
                .setInitialDelay(timeDiffBetweenNowAndTomorrow, TimeUnit.MILLISECONDS)
                .addTag(Constants.TAG_RANDOM_WALLPAPER)
                .build()
        WorkManager.getInstance().enqueue(randomWorkRequest)

    }

    companion object {

        const val ACTION_SETUP_WORKER = "ACTION_SETUP_WORKER"

        fun setupWorker(context: Context) {
            val intent = Intent(context, WallpaperService::class.java)
            intent.action = ACTION_SETUP_WORKER
            context.startService(intent)
        }
    }

}