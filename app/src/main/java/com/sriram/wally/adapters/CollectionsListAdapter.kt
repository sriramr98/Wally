package com.sriram.wally.adapters

import android.content.Context
import android.graphics.Color
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.sriram.wally.R
import com.sriram.wally.models.response.Collection
import com.sriram.wally.utils.DiffCallback
import com.sriram.wally.utils.Logger
import kotlinx.android.synthetic.main.item_collection.view.*
import java.lang.Exception

class CollectionsListAdapter(val context: Context, val picasso: Picasso) : RecyclerView.Adapter<CollectionsListAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private val images = arrayListOf<Collection>()
    private var mListener: PhotoListener? = null

    init {
        Logger.i(picasso)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_collection, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = images.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(images[position])

    fun setImages(images: ArrayList<Collection>) {
        val callback = DiffCallback(this.images, images)
        val result = DiffUtil.calculateDiff(callback)

        result.dispatchUpdatesTo(this)
        this.images.clear()
        this.images.addAll(images)
    }

    fun onItemClickListener(listener: PhotoListener) {
        mListener = listener
    }

    interface PhotoListener {
        fun onPhotoClicked(photo: Collection)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                mListener?.onPhotoClicked(images[adapterPosition])
            }
        }

        fun bind(image: Collection) {

            val placeholderColor = Color.parseColor(image.coverPhoto.color)
            // we are doing this since picasso does not support using color Int's as placeholders
            itemView.img_cover.setBackgroundColor(placeholderColor)

            Logger.i(image.coverPhoto.urls.regular!!)

            picasso.load(image.coverPhoto.urls.regular)
                    .error(android.R.color.black)
                    .into(itemView.img_cover, object : Callback {
                        override fun onSuccess() {
                            itemView.tv_title.text = image.title
                            itemView.tv_count.text = "${image.totalPhotos} photos"
                        }

                        override fun onError(e: Exception?) {
                            e?.printStackTrace()
                        }

                    })

        }
    }
}