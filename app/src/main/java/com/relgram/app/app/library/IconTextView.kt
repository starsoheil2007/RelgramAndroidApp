package com.relgram.app.app.library

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

/**
 * Text view with MaterialIconFont font
 *
 */
class IconTextView : AppCompatTextView {


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
                .getIconTypeface()
        //setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);

        //define different line space
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            setLineSpacing(0f, 1.3f)
        }

    }

}