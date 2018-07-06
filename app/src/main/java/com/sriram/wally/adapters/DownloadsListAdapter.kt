package com.sriram.wally.adapters

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.sriram.wally.R
import com.sriram.wally.models.ImageModel
import com.sriram.wally.utils.DiffCallback
import kotlinx.android.synthetic.main.item_image.view.*
import java.io.File

class DownloadsListAdapter(context: Context, val picasso: Picasso) : RecyclerView.Adapter<DownloadsListAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private val images = arrayListOf<ImageModel>()
    private var mListener: PhotoListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = images.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(images[position])

    fun setImages(images: ArrayList<ImageModel>) {
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
        fun onPhotoClicked(photo: ImageModel)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                mListener?.onPhotoClicked(images[adapterPosition])
            }
        }

        fun bind(image: ImageModel) {
            picasso.load(File(image.imagePath))
                    .config(Bitmap.Config.ARGB_8888)
                    .fit()
                    .centerInside()
                    .error(android.R.color.black)
                    .placeholder(android.R.color.darker_gray)
                    .into(itemView.item_image)
        }
    }
}