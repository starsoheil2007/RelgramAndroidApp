package com.relgram.app.app.library

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.widget.TextView

/**
 * Edit Text with persian custom font
 *
 */
class PersianEditText : AppCompatEditText {


    constructor(context: Context) : super(context) {
        if (!isInEditMode) addFont(context)

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        if (!isInEditMode) addFont(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        if (!isInEditMode) addFont(context)
    }


    /**
     * add font to edit text
     *
     * @param context: context of android application
     */
    private fun addFont(context: Context) {
        typeface = FontHelper.getInstance(context)
                .getPersianTextTypeface()
        //setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);

        //define different line space
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            setLineSpacing(0f, 1.3f)
        }

    }

    /**
     * customize set text function for set font
     *
     * @param text: text string
     * @param type: type of buffer
     */
    override fun setText(text: CharSequence?, type: TextView.BufferType) {
        var text = text
        if (text != null) text = FontHelper.toPersianNumber(text.toString())
        super.setText(text, type)
    }

}