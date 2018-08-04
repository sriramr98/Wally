package com.sriram.wally.ui.detail

import android.Manifest
import android.R.attr.uiOptions
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.github.florent37.runtimepermission.RuntimePermission.askPermission
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.sriram.wally.R
import com.sriram.wally.core.WallyService
import com.sriram.wally.models.ImageModel
import com.sriram.wally.models.response.PhotoListResponse
import com.sriram.wally.ui.InfoBottomSheet
import com.sriram.wally.utils.Logger
import com.sriram.wally.utils.isConnectedToNetwork
import com.sriram.wally.utils.isExternalStorageWritable
import kotlinx.android.synthetic.main.activity_image_detail.*
import org.jetbrains.anko.*
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject
import java.io.File
import java.lang.Exception


class ImageDetailActivity : AppCompatActivity() {

    companion object {
        const val ACTION_IMAGE_URL = "action-image-url"
        const val ACTION_IMAGE_FILE = "action-image-file"
        const val PHOTO_EXTRA = "photo_extra"
    }

    private val picasso by inject<Picasso>()
    private val mViewModel by viewModel<ImageDetailViewModel>()

    private val onImageLoaded = object : Callback {
        override fun onSuccess() {
            progress_bar.visibility = View.GONE
        }

        override fun onError(e: Exception?) {
            progress_bar.visibility = View.GONE
            e?.printStackTrace()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        if (intent == null || !intent.hasExtra(PHOTO_EXTRA)) {
            finish()
            return
        }

        if (intent.action == ACTION_IMAGE_URL) {
            loadImageFromUrl(intent.getParcelableExtra(PHOTO_EXTRA))
            setupFabMenu(false)
        } else {
            loadImageFromFile(intent.getParcelableExtra(PHOTO_EXTRA))
            setupFabMenu(true)
        }

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fab_menu.setOnActionSelectedListener {
            when (it.id) {
                R.id.fab_download -> {
                    // result is handled in the live data callback below
                    downloadImage()
                    false // true to keep the Speed Dial open
                }
                R.id.fab_info -> {
                    val infoSheet = mViewModel.imageData?.let { it1 -> InfoBottomSheet.instantiate(it1) }
                    infoSheet?.show(supportFragmentManager, InfoBottomSheet.TAG)
                    false
                }
                R.id.fab_set_wallpaper -> {
                    setWallpaper()
                    false
                }
                else -> false
            }
        }


//        tv_user_name.text = "By ${photo.user?.name ?: photo.user?.username ?: "N/A"}"

    }

    private fun loadImageFromFile(image: ImageModel) {
        mViewModel.loadData(image.id)

        picasso.load(File(image.imagePath))
                .config(Bitmap.Config.ARGB_8888)
                .fit()
                .centerInside()
                .into(img_photo, onImageLoaded)
    }

    private fun loadImageFromUrl(photo: PhotoListResponse?) {

        mViewModel.loadData(photo?.id!!)

        picasso.load(photo.urls?.regular)
                .into(img_photo, onImageLoaded)

    }

    private fun setWallpaper() {
        // true downloads the image and sets as wallpaper but does not save it to file
        downloadImage(true)
    }

    private fun downloadImage(setWallpaper: Boolean = false) {
        askPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE).onAccepted {
            if (isExternalStorageWritable()) {
                if (isConnectedToNetwork(this)) {
                    val downloadService = intentFor<WallyService>(WallyService.EXTRA_IMAGE_ID to mViewModel.getId(), WallyService.EXTRA_SET_WALLPAPER to setWallpaper)
                    downloadService.action = WallyService.ACTION_DOWNLOAD_IMAGE

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(downloadService)
                    } else {
                        startService(downloadService)
                    }

                    toast("Your image will be downloaded in the background")
                } else {
                    toast("No internet connection detected. Please try again")
                }
            } else {
                toast("Error cannot find external storage to download images")
            }
        }.onDenied { e ->
            alert("We need permission to access local storage for us to download and save the image. Please grant us the permissions", "Grant Permission") {
                okButton {
                    e.askAgain()
                }
                noButton {
                    it.dismiss()
                }
            }.show()
        }.onForeverDenied { e ->
            alert("We need permission to access local storage for us to download and save the image. Please grant us the permissions", "Grant Permission") {
                okButton {
                    e.goToSettings()
                }
                noButton {
                    it.dismiss()
                    toast("Error downloading due to insufficient permissions")
                }
            }.show()
        }.ask()
    }

    private fun setupFabMenu(isFile: Boolean = false) {
        val menuItems: ArrayList<SpeedDialActionItem> = arrayListOf()


        if (!isFile) {

            val downloadItem = SpeedDialActionItem.Builder(R.id.fab_download, R.drawable.ic_download_white)
                    .setLabel(getString(R.string.download))
                    .setLabelBackgroundColor(ResourcesCompat.getColor(resources, R.color.black, theme))
                    .setLabelColor(ResourcesCompat.getColor(resources, R.color.white, theme))
                    .setFabBackgroundColor(ResourcesCompat.getColor(resources, R.color.black, theme))
                    .create()

            menuItems.add(downloadItem)
        }

        val wallpaperItem = SpeedDialActionItem.Builder(R.id.fab_set_wallpaper, R.drawable.ic_wallpaper_white)
                .setLabel(getString(R.string.set_wallpaper))
                .setLabelBackgroundColor(ResourcesCompat.getColor(resources, R.color.black, theme))
                .setLabelColor(ResourcesCompat.getColor(resources, R.color.white, theme))
                .setFabBackgroundColor(ResourcesCompat.getColor(resources, R.color.black, theme))
                .create()

        menuItems.add(wallpaperItem)

        val infoItem = SpeedDialActionItem.Builder(R.id.fab_info, R.drawable.ic_info)
                .setLabel(getString(R.string.info))
                .setLabelBackgroundColor(ResourcesCompat.getColor(resources, R.color.black, theme))
                .setLabelColor(ResourcesCompat.getColor(resources, R.color.white, theme))
                .setFabBackgroundColor(ResourcesCompat.getColor(resources, R.color.black, theme))
                .create()

        menuItems.add(infoItem)

        fab_menu.addAllActionItems(menuItems)
    }

    private fun fullScreen() {

        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        val uiOptions = window.decorView.systemUiVisibility
        var newUiOptions = uiOptions
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        val isImmersiveModeEnabled = isImmersiveModeEnabled()
        if (isImmersiveModeEnabled) {
            Logger.i("Turning immersive mode mode off. ")
        } else {
            Logger.i("Turning immersive mode mode on.")
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = newUiOptions
        //END_INCLUDE (set_ui_flags)
    }

    private fun isImmersiveModeEnabled(): Boolean {
        return uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY == uiOptions
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return if (intent.action == ACTION_IMAGE_URL) {
            menuInflater.inflate(R.menu.menu_detail, menu)
            true
        } else {
            menuInflater.inflate(R.menu.menu_downloads, menu)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_download -> {
                toast("Downloading image")
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (fab_menu.isOpen) {
            fab_menu.close()
        } else {
            super.onBackPressed()
        }
    }
}
