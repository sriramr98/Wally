package com.sriram.wally.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Environment
import com.sriram.wally.db.ImagesRepo
import com.sriram.wally.models.ImageModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

// utility extension to save a downloaded bitmap. Specialised method which saves at a specific location for the uses of this app
fun Bitmap.saveToFile(id: String? = UUID.randomUUID().toString(), imagesRepo: ImagesRepo) {
    if (id == null) return

    val wallyFolder = getFolderOfDownloadedImages()
    if (!wallyFolder.exists()) {
        wallyFolder.mkdirs()
    }

    // create a new file to save the image in with name as id
    val imageFile = File(wallyFolder, "$id.jpeg")
    if (!imageFile.exists()) {
        imageFile.createNewFile()
    }
    try {
        val outputStream = FileOutputStream(imageFile)

        // compress the bitmap into the file
        this.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        // flush the stream
        outputStream.flush()
        // close the stream
        outputStream.close()

        // image is saved. Write to room to track
        imagesRepo.insertImage(ImageModel(id, imageFile.path))
    } catch (e: IOException) {
        e.printStackTrace()
    }

}


/**
 *  Returns a File that represents the folder where all images are downloaded
 */
fun getFolderOfDownloadedImages(): File {
    val externalStoragePath = Environment.getExternalStorageDirectory()

    // the reference to wallyFolder where all downloaded images are saved.
    val wallyFolder = File(externalStoragePath, "wally")

    // if the folder isn't present, create the folder
    if (!wallyFolder.exists()) wallyFolder.mkdirs()
    return wallyFolder
}

fun isExternalStorageWritable(): Boolean {
    return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}

fun isConnectedToNetwork(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}