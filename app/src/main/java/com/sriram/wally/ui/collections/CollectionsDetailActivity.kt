package com.sriram.wally.ui.collections

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.sriram.wally.R
import com.sriram.wally.adapters.CollectionItemListAdapter
import com.sriram.wally.models.NetworkStatus
import com.sriram.wally.models.response.Collection
import com.sriram.wally.models.response.PhotoListResponse
import kotlinx.android.synthetic.main.activity_collections_detail.*
import kotlinx.android.synthetic.main.content_collections_detail.*
import org.jetbrains.anko.toast
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

class CollectionsDetailActivity : AppCompatActivity() {

    companion object {
        const val TAG = "collection-detail"
    }

    private val mAdapter: CollectionItemListAdapter by inject()
    private val mViewModel: CollectionsDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections_detail)

        setSupportActionBar(toolbar)

        val collectionData = intent.getParcelableExtra<Collection>(TAG)
        if (collectionData == null) {
            toast("Error")
            finish()
        }

        supportActionBar?.title = collectionData.title
        tv_description.text = collectionData.description
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_collection_items.layoutManager = layoutManager
        rv_collection_items.adapter = mAdapter
        rv_collection_items.isNestedScrollingEnabled = true

        mViewModel.getPhotos(collectionData.id).observe(this, Observer {
            if (it == null || it.status == NetworkStatus.FAILURE) {
                failure()
            } else if (it.status == NetworkStatus.LOADING) {
                loading()
            } else {
                // success
                success(it.items)
            }
        })

    }

    private fun loading() {
        layout_refresh.isRefreshing = true
    }

    private fun failure() {
        layout_refresh.isRefreshing = false
        toast("Error loading data")
    }

    private fun success(photos: ArrayList<PhotoListResponse>) {
        layout_refresh.isRefreshing = false
        mAdapter.setImages(photos)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
