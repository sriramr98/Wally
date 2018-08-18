package com.sriram.wally.ui.search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sriram.wally.R
import com.sriram.wally.ui.MainActivity
import org.jetbrains.anko.longToast
import timber.log.Timber


class SearchActivity : AppCompatActivity() {

    companion object {
        const val CURRENT_CONTEXT = "current-context"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Get the intent, verify the action and get the query
        val intent = intent
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            val bundle = intent.getBundleExtra(SearchManager.APP_DATA)
            if (bundle != null) {
                val currentFragment = bundle.getString(CURRENT_CONTEXT)
                Timber.i("Search with tag $currentFragment")
                search(query.trim(), currentFragment)
            } else {
                Timber.i("Search without tag")
                search(query.trim())
            }
        }
    }

    private fun search(query: String, currentFragment: String = MainActivity.HOME_FRAGMENT) {
        longToast("$query $currentFragment")
    }
}
