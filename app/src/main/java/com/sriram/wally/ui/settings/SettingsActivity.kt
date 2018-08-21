package com.sriram.wally.ui.settings

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.sriram.wally.R
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.defaultSharedPreferences
import timber.log.Timber
import java.util.*

class SettingsActivity : AppCompatActivity() {

    companion object {
        private const val KEY_RANDOM_WALLPAPER_JOB = "key-random-wallpaper-job"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_settings, SettingsFragment())
                    .commit()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
