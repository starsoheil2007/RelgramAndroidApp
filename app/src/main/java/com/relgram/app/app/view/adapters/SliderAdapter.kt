package com.relgram.app.app.view.adapters

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.relgram.app.app.R
import com.relgram.app.app.library.ImageLoaderHelper
import com.relgram.app.app.webservice.WebService


class SliderAdapter(var context: Context, var imageList: List<String>, var isBanner: Boolean) : PagerAdapter() {


    var mLayoutInflater: LayoutInflater? = null

    init {
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater!!.inflate(R.layout.slider_view, container, false)
        val imageView = itemView.findViewById(R.id.image) as AppCompatImageView
        ImageLoaderHelper.displayImage(context, imageView, WebService.PICTURE_URL + imageList[position])
        if (isBanner) {
            imageView.scaleType = ImageView.ScaleType.FIT_XY
        }
        container.addView(itemView)
        return itemView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as AppCompatImageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as AppCompatImageView)
    }

    override fun getCount(): Int {
        return imageList.size
    }
}