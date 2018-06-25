package com.sriram.wally.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.sriram.wally.R
import com.sriram.wally.ui.home.PhotosListFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportActionBar?.setTitle(R.string.title_home)
                PhotosListFragment.instantiate().show(true, PhotosListFragment.TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                supportActionBar?.setTitle(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                supportActionBar?.setTitle(R.string.title_notifications)
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

        PhotosListFragment.instantiate().show()

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
