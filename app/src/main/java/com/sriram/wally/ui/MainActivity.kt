package com.sriram.wally.ui

import android.Manifest
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.github.florent37.runtimepermission.RuntimePermission.askPermission
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.sriram.wally.R
import com.sriram.wally.ui.collections.CollectionsFragment
import com.sriram.wally.ui.downloads.DownloadsFragment
import com.sriram.wally.ui.home.PhotosListFragment
import com.sriram.wally.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {

    private var currentFragmentTag = HOME_FRAGMENT

    companion object {
        const val HOME_FRAGMENT = "HOME"
        const val COLLECTIONS_FRAGMENT = "COLLECTIONS"
        const val DOWNLOADS_FRAGMENT = "DOWNLOADS"
        const val EXTRAS_SHOW_DOWNLOAD = "EXTRAS_SHOW_DOWNLOAD"
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (currentFragmentTag != HOME_FRAGMENT) {
                    supportActionBar?.setTitle(R.string.title_home)
                    PhotosListFragment.instantiate().show(true, PhotosListFragment.TAG)
                    currentFragmentTag = HOME_FRAGMENT
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_collections -> {
                if (currentFragmentTag != COLLECTIONS_FRAGMENT) {
                    supportActionBar?.setTitle(R.string.title_collections)
                    CollectionsFragment.instantiate().show(true, CollectionsFragment.TAG)
                    currentFragmentTag = COLLECTIONS_FRAGMENT
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_downloads -> {
                if (currentFragmentTag != DOWNLOADS_FRAGMENT) {
                    supportActionBar?.setTitle(R.string.title_downloads)
                    DownloadsFragment.instantiate().show(true, DownloadsFragment.TAG)
                    currentFragmentTag = DOWNLOADS_FRAGMENT
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
            val isDownloads = intent.getBooleanExtra(EXTRAS_SHOW_DOWNLOAD, false)
            currentFragmentTag = if (isDownloads) {
                DownloadsFragment.instantiate().show(false)
                 supportActionBar?.setTitle(R.string.title_downloads)
                DOWNLOADS_FRAGMENT
            } else {
                PhotosListFragment.instantiate().show()
                supportActionBar?.setTitle(R.string.title_home)
                HOME_FRAGMENT
            }
        }

        // just ask for permission once. Do not need to handle rejection as the specific permissions
        // will again be asked when required
        askPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.SET_WALLPAPER).ask()

        search_view.setHint("Search Wally")
        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null) return false
                val searchIntent = intentFor<SearchActivity>(SearchActivity.CURRENT_CONTEXT to currentFragmentTag, SearchManager.QUERY to query)
                searchIntent.action = Intent.ACTION_SEARCH
                startActivity(searchIntent)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val item = menu?.findItem(R.id.action_search)
        search_view.setMenuItem(item)

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

    override fun onBackPressed() {
        if (search_view.isSearchOpen) {
            search_view.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

}
