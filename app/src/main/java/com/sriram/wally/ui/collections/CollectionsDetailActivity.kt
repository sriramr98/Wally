package com.sriram.wally.ui.collections

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.sriram.wally.R
import com.sriram.wally.adapters.PhotoListAdapter
import com.sriram.wally.models.NetworkStatus
import com.sriram.wally.models.response.Collection
import com.sriram.wally.models.response.PhotoListResponse
import com.sriram.wally.utils.EndlessScrollRvListener
import kotlinx.android.synthetic.main.activity_collections_detail.*
import kotlinx.android.synthetic.main.fragment_photos_list.*
import org.jetbrains.anko.toast
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

class CollectionsDetailActivity : AppCompatActivity() {

    companion object {
        const val TAG = "collection-detail"
    }

    private val mAdapter: PhotoListAdapter by inject()
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_collection_items.layoutManager = layoutManager
        rv_collection_items.adapter = mAdapter

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

        rv_collection_items.addOnScrollListener(object : EndlessScrollRvListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                mViewModel.refresh()
            }

        })

    }

    private fun loading() {
        layout_refresh.isRefreshing = true
    }

    private fun failure() {
        layout_refresh.isRefreshing = false
        if (mAdapter.getImages().isEmpty()) {
            toast("Error loading data")
        }
    }

    private fun success(photos: ArrayList<PhotoListResponse>) {
        layout_refresh.isRefreshing = false
        toast(photos.size.toString())
        mAdapter.setImages(photos)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
