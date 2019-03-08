package com.relgram.app.app.library

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener

/**
 * Image loader from server handler
 * This class has functions for download and show images from server
 * Base on Glide library
 */
object ImageLoaderHelper {


    /**
     * show image on imageView
     *
     * @param context context of android application
     * @param imageView Android image view
     * @param imageUrl full url of image
     * @param defaultImageResourceId default image if can't load main image from server
     * @param listener Glide listener for handle errors
     */
    fun displayImage(context: Context, imageView: ImageView, imageUrl: String, defaultImageResourceId: Int?, listener: RequestListener<Drawable>?) {

        var builder = GlideApp.with(context)
                .load(imageUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(listener)

        if (defaultImageResourceId != null) {
            builder = builder.error(defaultImageResourceId)
        }
        builder.into(imageView)
    }

    /**
     * Show image without listener and default image
     *
     * @param context context of android application
     * @param imageView Android image view
     * @param imageUrl full url of image
     */

    fun displayImage(context: Context, imageView: ImageView, imageUrl: String) {
        displayImage(context, imageView, imageUrl, null, null)
    }

    /**
     *  Show Circle image from uri
     *
     * @param context context of android application
     * @param imageView Android image view
     * @param imageUrl full uri of image (uri type)
     */

    fun displayCircleImage(context: Context, imageView: ImageView, imageUrl: Uri) {
        GlideApp.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(CropCircleTransformation())
                .into(imageView)
    }

    /**
     * Show Circle image from web url
     *
     * @param context context of android application
     * @param imageView Android image view
     * @param imageUrl full url of image(String type)
     */

    fun displayCircleImage(context: Context, imageView: ImageView, imageUrl: String) {
        GlideApp.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(CropCircleTransformation())
                .into(imageView)
    }

}