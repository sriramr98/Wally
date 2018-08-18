package com.sriram.wally.ui

import android.Manifest
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import com.github.florent37.runtimepermission.RuntimePermission.askPermission
import com.sriram.wally.R
import com.sriram.wally.ui.collections.CollectionsFragment
import com.sriram.wally.ui.downloads.DownloadsFragment
import com.sriram.wally.ui.home.PhotosListFragment
import com.sriram.wally.ui.search.SearchActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import timber.log.Timber
import android.support.v4.view.MenuItemCompat.getActionView



class MainActivity : AppCompatActivity() {

    private var currentFragmentTag = HOME_FRAGMENT

    companion object {
        const val HOME_FRAGMENT = "HOME"
        const val COLLECTIONS_FRAGMENT = "COLLECTIONS"
        const val DOWNLOADS_FRAGMENT = "DOWNLOADS"
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
            PhotosListFragment.instantiate().show()
        }

        // just ask for permission once. Do not need to handle rejection as the specific permissions
        // will again be asked when required
        askPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_WALLPAPER).ask()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false) // Do not iconify the widget; expand it by default

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

    override fun onSearchRequested(): Boolean {
        Timber.i("Search Requested")
        val data = Bundle()
        data.putString(SearchActivity.CURRENT_CONTEXT, currentFragmentTag)
        startSearch(null, false, data, false)
        return true
    }
}
