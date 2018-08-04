package com.sriram.wally.ui.home

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sriram.wally.R
import com.sriram.wally.adapters.PhotoListAdapter
import com.sriram.wally.models.NetworkStatus
import com.sriram.wally.models.response.PhotoListResponse
import com.sriram.wally.ui.detail.ImageDetailActivity
import com.sriram.wally.utils.EndlessScrollRvListener
import com.sriram.wally.utils.isConnectedToNetwork
import kotlinx.android.synthetic.main.fragment_photos_list.*
import org.jetbrains.anko.support.v4.intentFor
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

class PhotosListFragment : Fragment() {

    companion object {
        fun instantiate(): PhotosListFragment {
            return PhotosListFragment()
        }

        const val TAG = "PhotosListFragment"
    }

    val mViewModel by viewModel<PhotosListViewModel>()
    private val mAdapter by inject<PhotoListAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photos_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)

        rv_images.layoutManager = layoutManager
        rv_images.adapter = mAdapter

        mAdapter.onItemClickListener(object : PhotoListAdapter.PhotoListener {
            override fun onPhotoClicked(photo: PhotoListResponse) {
                val intent = intentFor<ImageDetailActivity>(ImageDetailActivity.PHOTO_EXTRA to photo)
                intent.action = ImageDetailActivity.ACTION_IMAGE_URL
                startActivity(intent)
            }
        })

        toggleLoading(true)

        mViewModel.getPhotosData().observe(this, Observer {
            if (it == null || it.status == NetworkStatus.LOADING) {
                toggleLoading(true)
            } else if (it.status == NetworkStatus.FAILURE && it.items.isEmpty()) {
                if (isConnectedToNetwork(requireContext())) {
                    showError("Error getting photos. Please try again")
                } else {
                    showError("No Internet connection. Please try again")
                }
            } else if (it.status == NetworkStatus.SUCCESS) {
                showSuccess(it.items)
            }
        })

//        layout_refresh_photos_list.setOnRefreshListener {
//            mViewModel.fromStart()
//        }

        rv_images.addOnScrollListener(object : EndlessScrollRvListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                mViewModel.refresh()
            }

        })

    }


    private fun showSuccess(images: ArrayList<PhotoListResponse>) {
        toggleLoading(false)
        if (images.isEmpty()) {
            showError("No images")
            return
        }
        rv_images.visibility = View.VISIBLE
        layout_error.visibility = View.GONE
        mAdapter.setImages(images)
    }

    private fun showError(message: String) {
        toggleLoading(false)
        rv_images.visibility = View.GONE
        layout_error.visibility = View.VISIBLE
        layout_error.text = message
    }

    private fun toggleLoading(isRefreshing: Boolean) {
//        layout_refresh_photos_list.isRefreshing = isRefreshing
        rv_images.visibility = View.VISIBLE
        layout_error.visibility = View.GONE
    }
}