package com.relgram.app.app.view_models

import com.relgram.app.view_models.BaseViewModel

/**
 * Loading Dialog View Model
 * please add text by call bind option
 *
 */
class DialogLoadingViewModel : BaseViewModel() {

    var loadingText: String? = null

    /**
     * binding text to loading dialog
     *
     * @param text text will be show
     */
    fun bind(text: String) {
        loadingText = text
    }
}