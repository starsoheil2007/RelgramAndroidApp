package com.relgram.app.app.library

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * Rounded Image Transformation for Glide
 * This code copied from StackOverFlow
 */
class RoundedCornersTransformation(private var radius: Int, private var margin: Int) : BitmapTransformation() {
    private val ID = "com.soheil_tayyeb.hamsan.app.library.RoundedCornersTransformation"
    private val ID_BYTES = ID.toByteArray(Charset.forName("UTF-8"))


    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {

        val width = toTransform.width
        val height = toTransform.height

        var bitmap: Bitmap? = pool.get(width, height, Bitmap.Config.ARGB_8888)
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap!!)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        canvas.drawRoundRect(RectF(margin.toFloat(), margin.toFloat(), (width - margin).toFloat(), (height - margin).toFloat()), radius.toFloat(), radius.toFloat(), paint)

        return bitmap
    }

    override fun equals(other: Any?): Boolean {
        return other is RoundedCornersTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

}