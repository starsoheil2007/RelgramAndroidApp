package com.relgram.app.app.library

import android.content.Context
import android.graphics.Typeface

/**
 * a class to change whole application font ever time you want
 *
 */
open class FontHelper {

    /**
     * icon Font TypeFace
     */
    private var iconTypeface: Typeface
    /**
     * persian Font TypeFace
     */
    private var persianTypeface: Typeface

    companion object {
        private val persianNumbers = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
        lateinit var instance: FontHelper
            private set

        @Synchronized
        fun getInstance(context: Context): FontHelper {
            instance = FontHelper(context)
            return instance
        }

        /**
         * Convert english numbers to persian unicode number
         *
         * @param text: text that include numbers
         */
        fun toPersianNumber(text: String): String {
            if ("" == text) return ""
            var out = ""
            val length = text.length
            for (i in 0 until length) {
                val c = text[i]
                if ('0' <= c && c <= '9') {
                    val number = Integer.parseInt(c.toString())
                    out += persianNumbers[number]
                } else if (c == '٫') {
                    out += '،'.toString()
                } else {
                    out += c
                }

            }
            return out
        }
    }


    constructor(context: Context) {
        iconTypeface = Typeface.createFromAsset(context.assets, "fonts/MaterialIcons.ttf")
        persianTypeface = Typeface.createFromAsset(context.assets, "fonts/iransans.ttf")
    }

    /**
     * get Icon TypeFace for set
     *
     * @return typeface
     */
    fun getIconTypeface(): Typeface {
        return iconTypeface
    }

    /**
     * get Persian TypeFace for set
     *
     * @return typeface
     */
    fun getPersianTextTypeface(): Typeface {
        return persianTypeface
    }


}