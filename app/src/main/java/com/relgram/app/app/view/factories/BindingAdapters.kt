package com.relgram.app.app.view.factories

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.relgram.app.app.HamsanApp
import com.relgram.app.app.library.ImageLoaderHelper

/**
 * set adapter to RecyclerView in data binding
 *
 * @param view RecyclerView
 * @param adapter Adapter of RecyclerView
 */
@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

/**
 * show normal image to imageview
 *
 * @param view image view
 * @param imageUrl full url of image
 */
@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String) {
    ImageLoaderHelper.displayImage(HamsanApp.context, imageUrl = imageUrl, imageView = view)
}

/**
 * show circle image to imageview
 *
 * @param view image view
 * @param imageUrl full url of image
 */
@BindingAdapter("imageUrlCircle")
fun loadImageCircle(view: ImageView, imageUrl: String) {
    ImageLoaderHelper.displayCircleImage(HamsanApp.context, view, imageUrl)
}