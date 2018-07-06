package com.sriram.wally.ui.downloads

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sriram.wally.R
import com.sriram.wally.adapters.DownloadsListAdapter
import com.sriram.wally.models.ImageModel
import com.sriram.wally.ui.detail.ImageDetailActivity
import kotlinx.android.synthetic.main.fragment_downloads.*
import org.jetbrains.anko.support.v4.intentFor
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

class DownloadsFragment : Fragment() {

    private val mViewModel by viewModel<DownloadsViewModel>()
    private val mAdapter by inject<DownloadsListAdapter>()

    companion object {
        const val TAG = "downloads-fragment"

        fun instantiate(): DownloadsFragment {
            return DownloadsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_downloads, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)

        rv_downloads.layoutManager = layoutManager
        rv_downloads.adapter = mAdapter

        mViewModel.getAllDownloadedImages().observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                mAdapter.setImages(ArrayList(it))
            }
        })

        mAdapter.onItemClickListener(object : DownloadsListAdapter.PhotoListener {
            override fun onPhotoClicked(photo: ImageModel) {
                val intent = intentFor<ImageDetailActivity>(ImageDetailActivity.PHOTO_EXTRA to photo)
                intent.action = ImageDetailActivity.ACTION_IMAGE_FILE
                startActivity(intent)
            }
        })

    }
}