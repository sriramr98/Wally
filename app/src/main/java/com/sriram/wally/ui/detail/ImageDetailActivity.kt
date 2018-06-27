package com.sriram.wally.ui.detail

import android.R.attr.uiOptions
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.sriram.wally.R
import com.sriram.wally.models.response.PhotoListResponse
import com.sriram.wally.utils.Logger
import kotlinx.android.synthetic.main.activity_image_detail.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import java.lang.Exception


class ImageDetailActivity : AppCompatActivity() {

    companion object {
        const val PHOTO_EXTRA = "photo_extra"
    }

    private val picasso by inject<Picasso>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        if (intent == null || !intent.hasExtra(PHOTO_EXTRA)) {
            finish()
            return
        }

        val photo = intent.getParcelableExtra<PhotoListResponse>(PHOTO_EXTRA)
        if (photo == null) {
            finish()
            return
        }

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupFabMenu()

        picasso.load(photo.urls?.regular)
                .into(img_photo, object : Callback {
                    override fun onSuccess() {
                        progress_bar.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        progress_bar.visibility = View.GONE
                        e?.printStackTrace()
                    }

                })


        fab_menu.setOnActionSelectedListener {
            when (it.id) {
                R.id.fab_download -> {
                    toast("Downloading!")
                    false // true to keep the Speed Dial open
                }
                R.id.fab_info -> {
                    toast("More info!")
                    false
                }
                R.id.fab_set_wallpaper -> {
                    toast("Wallpaper!")
                    false
                }
                else -> false
            }
        }

//        tv_user_name.text = "By ${photo.user?.name ?: photo.user?.username ?: "N/A"}"


    }

    private fun setupFabMenu() {
        val menuItems: ArrayList<SpeedDialActionItem> = arrayListOf()

        val downloadItem = SpeedDialActionItem.Builder(R.id.fab_download, R.drawable.ic_download_white)
                .setLabel(getString(R.string.download))
                .setLabelBackgroundColor(ResourcesCompat.getColor(resources, R.color.black, theme))
                .setLabelColor(ResourcesCompat.getColor(resources, R.color.white, theme))
                .setFabBackgroundColor(ResourcesCompat.getColor(resources, R.color.black, theme))
                .create()

        menuItems.add(downloadItem)

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
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_download -> {
                toast("Downloading image")
                true
            }
            android.R.id.home -> {
                onBackPressed()
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
