package com.sriram.wally.ui.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.sriram.wally.R
import com.sriram.wally.models.response.PhotoListResponse
import kotlinx.android.synthetic.main.activity_image_detail.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import java.lang.Exception

class ImageDetailActivity : AppCompatActivity() {

    companion object {
        const val PHOTO_EXTRA = "photo-extra"
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
}
