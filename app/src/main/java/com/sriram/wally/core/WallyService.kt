package com.sriram.wally.core

import android.app.IntentService
import android.app.PendingIntent
import android.app.WallpaperManager
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.squareup.picasso.Picasso
import com.sriram.wally.R
import com.sriram.wally.db.ImagesRepo
import com.sriram.wally.networking.NetworkRepo
import com.sriram.wally.ui.MainActivity
import com.sriram.wally.utils.Constants
import com.sriram.wally.utils.saveToFile
import org.jetbrains.anko.notificationManager
import org.koin.android.ext.android.inject
import java.net.SocketTimeoutException


class WallyService : IntentService(NAME) {

    private val networkRepo by inject<NetworkRepo>()
    private val imagesRepo by inject<ImagesRepo>()
    private val picasso by inject<Picasso>()

    companion object {
        private const val NAME = "WallyService"
        const val ACTION_DOWNLOAD_IMAGE = "download-image"
        const val EXTRA_IMAGE_ID = "image-id"
        const val DOWNLOAD_NOTIFICATION_ID = 2334
        const val EXTRA_SET_WALLPAPER = "set-wallpaper"

        const val ACTION_SCHEDULE_WALLPAPER = "action-schedule-wallpaper"
        const val ACTION_RANDOM_WALLPAPER = "action-random-wallpaper"

    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notification = NotificationCompat.Builder(this, Constants.IMAGE_DOWNLOAD_CHANNEL)
                    .setContentTitle("Downloading Image")
                    .setContentText("Starting image download. Please wait").build()

            startForeground(1, notification)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent?.action == ACTION_DOWNLOAD_IMAGE) {
            downloadImage(intent.getStringExtra(EXTRA_IMAGE_ID), intent.getBooleanExtra(EXTRA_SET_WALLPAPER, false))
        } else if (intent?.action == ACTION_SCHEDULE_WALLPAPER) {
            scheduleWallpaper()
        } else if (intent?.action == ACTION_RANDOM_WALLPAPER) {
            scheduleRandomWallpaper()
        }
    }

    private fun scheduleRandomWallpaper() {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val workRequest = OneTimeWorkRequestBuilder<RandomWallpaperWorker>()
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance().enqueue(workRequest)
    }

    private fun scheduleWallpaper() {
        val mBuilder = NotificationCompat.Builder(this, Constants.IMAGE_DOWNLOAD_CHANNEL)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle("Daily wallpaper")
                .setContentText("Hey here is your daily wallpaper being fired at 9AM")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, mBuilder.build())
    }

    private fun downloadImage(id: String, setWallpaper: Boolean = false) {

        // prepare notifications
        val mBuilder = NotificationCompat.Builder(this, Constants.IMAGE_DOWNLOAD_CHANNEL)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle(getString(R.string.downloading_image_title))
                .setContentText(getString(R.string.downloading))
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (!setWallpaper) {
            val notifyIntent = Intent(this, MainActivity::class.java)
            // Set the Activity to start in a new, empty task
            notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            notifyIntent.putExtra(MainActivity.EXTRAS_SHOW_DOWNLOAD, true)
            // Create the PendingIntent
            val notifyPendingIntent = PendingIntent.getActivity(
                    this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            mBuilder.setContentIntent(notifyPendingIntent)
        }

        mBuilder.setProgress(0, 0, true).setOngoing(true)
        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, mBuilder.build())


        try {
            val response = networkRepo.getDownloadsEndpoint(id).execute()
            if (response.isSuccessful) {
                // success
                val url = response.body()?.url
                val image = picasso.load(url)
                        .get()
                if (image == null) {
                    // failure
                    mBuilder.setContentText("Download failed. Please try again later")
                            .setProgress(0, 0, false).setOngoing(false)
                    notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, mBuilder.build())
                } else {
                    // success
                    if (setWallpaper) {
                        // This action is to set wallpaper. So we just set as wallpaper without saving to file
                        val wallpaperManager = WallpaperManager.getInstance(this)
                        wallpaperManager.setBitmap(image)
                        mBuilder.setContentText("Wallpaper success.")
                                .setProgress(0, 0, false).setOngoing(false)
                        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, mBuilder.build())

                    } else {
                        // the action is to download. So we save it to file
                        image.saveToFile(id, imagesRepo)
                        mBuilder.setContentText("Download success. You can view your downloaded images in the downloads section")
                                .setProgress(0, 0, false).setOngoing(false)
                        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, mBuilder.build())
                    }
                }
            } else {
                // failure
                val notification = mBuilder.setContentText("Download failed. Please try again later")
                        .setProgress(0, 0, false).setOngoing(false).build()
                notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, notification)
            }
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            val notification = mBuilder.setContentText("Download failed due to error in network. Please try again later.")
                    .setProgress(0, 0, false).setOngoing(false)
                    .build()
            notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, notification)
        }


    }

}