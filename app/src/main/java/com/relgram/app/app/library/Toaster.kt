package com.relgram.app.app.library

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.relgram.app.app.R
import com.relgram.app.app.library.Toaster.Companion.toast

/**
 * Customize Android Toast
 * @property toast: static function based on your parameters
 *
 */
class Toaster {

    companion object {
        /**
         * Show Toast
         *
         * @param context: Context of android application
         * @param message: message will be showed
         * @param duration: time to be show in milliseconds
         */
        fun toast(context: Context, message: String, duration: Int) {
            val layout = LayoutInflater.from(context)
                    .inflate(R.layout.custom_toast, null)
            val text = layout.findViewById(R.id.message) as TextView
            text.text = message

            val toast = Toast(context)
            toast.view = layout
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.duration = duration
            toast.show()
        }

        /**
         * Show Toast By String Id and time
         *
         * @param context: Context of android application
         * @param messageResId: id of string resource
         * @param duration: Time in milliseconds
         */

        fun toast(context: Context, messageResId: Int, duration: Int) {
            toast(context, context.getString(messageResId), duration)
        }

        /**
         * Show toast text String
         *
         * @param context: Context of android application
         * @param message: String Message
         */

        fun toast(context: Context, message: String) {
            toast(context, message, Toast.LENGTH_SHORT)
        }

        /**
         * Show toast text resource id
         *
         * @param context: Context of android application
         * @param messageResId:id of string resource
         */

        fun toast(context: Context, messageResId: Int) {
            toast(context, context.getString(messageResId), Toast.LENGTH_SHORT)
        }
    }
}