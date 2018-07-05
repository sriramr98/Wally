package com.sriram.wally.ui

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.sriram.wally.R
import com.sriram.wally.models.response.PhotoDetailResponse
import com.sriram.wally.utils.Logger
import kotlinx.android.synthetic.main.sheet_image_info.*
import org.jetbrains.anko.support.v4.toast
import org.koin.android.ext.android.inject

class InfoBottomSheet : BottomSheetDialogFragment() {

    private val picasso by inject<Picasso>()

    companion object {
        fun instantiate(imageData: PhotoDetailResponse): InfoBottomSheet {
            val infoSheet = InfoBottomSheet()
            val args = Bundle()
            args.putParcelable(IMAGE_DATA, imageData)
            infoSheet.arguments = args
            return infoSheet
        }

        const val IMAGE_DATA = "image-data"
        const val TAG = "info-bottom-sheet"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sheet_image_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments == null || arguments?.containsKey(IMAGE_DATA) == false) {
            return
        }

        val imageData = arguments?.getParcelable<PhotoDetailResponse>(IMAGE_DATA)

        setupUi(imageData)

        btn_images_artist.setOnClickListener {
            toast("This feature will be available soon")
        }
    }

    private fun setupUi(imageData: PhotoDetailResponse?) {
        Logger.e(imageData.toString())

        if (imageData == null) return
        picasso.load(imageData.user?.profileImages?.large)
                .error(R.color.black)
                .into(img_artist)

        // User details

        if (imageData.user == null) {
            tv_image_by.text = "Image by N/A"
            layout_user.visibility = View.GONE
            btn_images_artist.visibility = View.GONE
        } else {
            btn_images_artist.visibility = View.VISIBLE
            btn_images_artist.text = "More Images by ${imageData.user.name}"
            layout_user.visibility = View.VISIBLE
            tv_image_by.text = "Image by ${imageData.user.name}"
            tv_artist_name.text = "Name : ${imageData.user.name}"
            tv_artist_location.text = "Location : ${imageData.user.location}"
            tv_artist_unsplash_username.text = "Unsplash Username : ${imageData.user.username}"
        }

        // EXIF details

        if (imageData.exif == null) {
            layout_exif.visibility = View.GONE
        } else {
            layout_exif.visibility = View.VISIBLE
            tv_camera_make.text = "Make: ${imageData.exif.make}"
            tv_camera_model.text = "Model: ${imageData.exif.model}"
            tv_camera_exposure.text = "Exposure Time: ${imageData.exif.exposureTime}"
            tv_camera_aperture.text = "Aperture: ${imageData.exif.aperture}"
            tv_camera_focal.text = "Focal Length: ${imageData.exif.focalLength}"
        }

        // IMAGE details
        tv_image_description.text = imageData.description
        tv_width.text = "Width: ${imageData.width}"
        tv_height.text = "Height: ${imageData.height}"
        tv_likes.text = "Likes: ${imageData.likes}"
        tv_views.text = "Views: ${imageData.downloads}"
        tv_downloads.text = "Views: ${imageData.downloads}"

    }


}