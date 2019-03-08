package com.relgram.app.app.library

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * Scaled Image Transformation for Glide
 * This code copied from StackOverFlow
 */
class ScaleTransformation : BitmapTransformation {

    private val ID = "com.soheil_tayyeb.hamsan.app.library.ScaleTransformation"
    private val ID_BYTES = ID.toByteArray(Charset.forName("UTF-8"))

    private var width: Int = 0

    constructor(width: Int) {
        this.width = width

    }


    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        if (toTransform.width < width) {
            val height = (width.toFloat() / toTransform.width * toTransform.height).toInt()
            return Bitmap.createScaledBitmap(toTransform, width, height, false)
        } else {
            return toTransform
        }
    }


    override fun equals(other: Any?): Boolean {
        return other is RoundedCornersTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

}