package com.sriram.wally.ui.search

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.sriram.wally.R
import com.sriram.wally.adapters.CollectionsListAdapter
import com.sriram.wally.adapters.PhotoListAdapter
import com.sriram.wally.models.NetworkStatus
import com.sriram.wally.ui.MainActivity
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject


class SearchActivity : AppCompatActivity() {

    companion object {
        const val CURRENT_CONTEXT = "current-context"
        const val SEARCH_PHOTOS = "search-photos"
        const val SEARCH_COLLECTIONS = "search-collections"
    }

    private val mViewModel: SearchViewModel by viewModel()
    private val mCollectionsAdapter by inject<CollectionsListAdapter>()
    private val mPhotosAdapter by inject<PhotoListAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Get the intent, verify the action and get the query
        val intent = intent
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            val extra = intent.getStringExtra(CURRENT_CONTEXT)

            if (extra == null) {
                search(query)
            } else {
                when (extra) {
                    MainActivity.COLLECTIONS_FRAGMENT -> search(query, SEARCH_COLLECTIONS)
                    else -> search(query, SEARCH_PHOTOS)
                }
            }

        } else {
            onBackPressed()
        }


        mViewModel.getImages().observe(this, Observer {
            if (it == null || it.status == NetworkStatus.FAILURE) {
                handleFailure()
            } else if (it.status == NetworkStatus.LOADING) {
                handleLoading()
            } else {
                mPhotosAdapter.setImages(it.items)
                layout_refresh.isRefreshing = false
            }
        })

        mViewModel.getCollections().observe(this, Observer {
            if (it == null || it.status == NetworkStatus.FAILURE) {
                handleFailure()
            } else if (it.status == NetworkStatus.LOADING) {
                handleLoading()
            } else {
                mCollectionsAdapter.setImages(it.items)
                layout_refresh.isRefreshing = false
            }
        })

    }

    private fun handleLoading() {
        layout_refresh.isRefreshing = true
    }

    private fun handleFailure() {
        layout_refresh.isRefreshing = false
    }

    private fun search(query: String, type: String = SEARCH_PHOTOS) {
        mViewModel.setQueryAndType(query, type)
        setupUi(query, type)
    }

    private fun setupUi(query: String, type: String) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = query
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rv_images.layoutManager = layoutManager

        if (type == SEARCH_PHOTOS) {
            rv_images.adapter = mPhotosAdapter
        } else {
            rv_images.adapter = mCollectionsAdapter
        }

    }
}
