package com.sriram.wally.core

import android.app.IntentService
import android.app.WallpaperManager
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.squareup.picasso.Picasso
import com.sriram.wally.R
import com.sriram.wally.db.ImagesRepo
import com.sriram.wally.networking.NetworkRepo
import com.sriram.wally.utils.Constants
import com.sriram.wally.utils.saveToFile
import org.jetbrains.anko.notificationManager
import org.koin.android.ext.android.inject

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
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent?.action == ACTION_DOWNLOAD_IMAGE) {
            downloadImage(intent.getStringExtra(EXTRA_IMAGE_ID), intent.getBooleanExtra(EXTRA_SET_WALLPAPER, false))
        }
    }

    private fun downloadImage(id: String, setWallpaper: Boolean = false) {

        // prepare notifications
        val mBuilder = NotificationCompat.Builder(this, Constants.IMAGE_DOWNLOAD_CHANNEL)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle(getString(R.string.downloading_image_title))
                .setContentText(getString(R.string.downloading))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        mBuilder.setProgress(0, 0, true).setOngoing(true)
        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, mBuilder.build())


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
            mBuilder.setContentText("Download failed. Please try again later")
                    .setProgress(0, 0, false).setOngoing(false)
        }


    }

}