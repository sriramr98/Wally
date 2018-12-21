package com.sriram.wally.core

import android.app.WallpaperManager
import android.support.v4.app.NotificationCompat
import androidx.work.Worker
import com.squareup.picasso.Picasso
import com.sriram.wally.R
import com.sriram.wally.networking.NetworkRepo
import com.sriram.wally.utils.Constants
import com.sriram.wally.utils.SharedPrefUtils
import org.jetbrains.anko.notificationManager
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.net.SocketTimeoutException

class RandomWallpaperWorker : Worker(), KoinComponent {

    companion object {
        private const val WALLPAPER_NOTIFICATION_ID = 1445
    }

    override fun doWork(): Result {
        val wallyRepo by inject<NetworkRepo>()
        val picasso by inject<Picasso>()

        val notificationBuilder = NotificationCompat.Builder(applicationContext, Constants.IMAGE_DOWNLOAD_CHANNEL)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle(applicationContext.getString(R.string.downloading_image_title))
                .setContentText(applicationContext.getString(R.string.downloading_wallpaper_today))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setProgress(0, 0, true)
                .setOngoing(true)


        applicationContext.notificationManager.notify(WALLPAPER_NOTIFICATION_ID, notificationBuilder.build())

        try {
//            SharedPrefUtils.setIsRandomWallpaperScheduled(true)
            val response = wallyRepo.getRandomWallpape().execute()

            if (response == null || !response.isSuccessful || response.body() == null) {
                return Result.RETRY
            }

            val photo = response.body()

            val imageBitmap = picasso.load(photo?.urls?.full).get()

            return if (imageBitmap != null) {
                val wallpaperManger = WallpaperManager.getInstance(applicationContext)
                wallpaperManger.setBitmap(imageBitmap)

                notificationBuilder.setContentTitle(applicationContext.getString(R.string.wallpaper_complete))
                        .setContentText(applicationContext.getString(R.string.wallpaper_complete_text))
                        .setProgress(0, 0, false)
                        .setOngoing(false)
                        .setAutoCancel(true)

                applicationContext.notificationManager.notify(WALLPAPER_NOTIFICATION_ID, notificationBuilder.build())

                Result.SUCCESS
            } else {
                applicationContext.notificationManager.cancel(WALLPAPER_NOTIFICATION_ID)
                Result.FAILURE
            }
        } catch (e: SocketTimeoutException) {
            applicationContext.notificationManager.cancel(WALLPAPER_NOTIFICATION_ID)
            return Result.FAILURE
        }

    }

}