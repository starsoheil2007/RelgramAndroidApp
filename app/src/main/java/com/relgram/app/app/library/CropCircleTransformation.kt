package com.relgram.app.app.library

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * Circle Transformation for Glide
 * This code copied from StackOverFlow
 */
class CropCircleTransformation : BitmapTransformation {
    private val ID = "com.soheil_tayyeb.hamsan.app.library.CropCircleTransformation"
    private val ID_BYTES = ID.toByteArray(Charset.forName("UTF-8"))


    constructor() {}


    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val size = Math.min(toTransform.getWidth(), toTransform.getHeight())

        val width = (toTransform.getWidth() - size) / 2
        val height = (toTransform.getHeight() - size) / 2

        var bitmap: Bitmap? = pool.get(size, size, Bitmap.Config.ARGB_8888)
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap!!)
        val paint = Paint()
        val shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        if (width != 0 || height != 0) {
            val matrix = Matrix()
            matrix.setTranslate((-width).toFloat(), (-height).toFloat())
            shader.setLocalMatrix(matrix)
        }
        paint.shader = shader
        paint.isAntiAlias = true

        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)

        return bitmap
    }

    override fun equals(other: Any?): Boolean {
        return other is RoundedCornersTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }
}