package com.sriram.wally.ui

import android.Manifest
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.github.florent37.runtimepermission.RuntimePermission.askPermission
import com.sriram.wally.R
import com.sriram.wally.ui.collections.CollectionsFragment
import com.sriram.wally.ui.downloads.DownloadsFragment
import com.sriram.wally.ui.home.PhotosListFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private var currentFragmentTag = "HOME"

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (currentFragmentTag != "HOME") {
                    supportActionBar?.setTitle(R.string.title_home)
                    PhotosListFragment.instantiate().show(true, PhotosListFragment.TAG)
                    currentFragmentTag = "HOME"
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_collections -> {
                if (currentFragmentTag != "COLLECTIONS") {
                    supportActionBar?.setTitle(R.string.title_collections)
                    CollectionsFragment.instantiate().show(true, CollectionsFragment.TAG)
                    currentFragmentTag = "COLLECTIONS"
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_downloads -> {
                if (currentFragmentTag != "DOWNLOADS") {
                    supportActionBar?.setTitle(R.string.title_downloads)
                    DownloadsFragment.instantiate().show(true, DownloadsFragment.TAG)
                    currentFragmentTag = "DOWNLOADS"
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.title_home)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            PhotosListFragment.instantiate().show()
        }

        // just ask for permission once. Do not need to handle rejection as the specific permissions
        // will again be asked when required
        askPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_WALLPAPER).ask()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_settings -> {
                toast("Settings will be available soon").show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun Fragment.show(addToBackStack: Boolean = false, tag: String = "") {
        val transaction = supportFragmentManager.beginTransaction()
                .replace(R.id.frame_main, this)

        if (addToBackStack && tag.isNotBlank()) {
            transaction.addToBackStack(tag)
        }

        transaction.commit()
    }

}
