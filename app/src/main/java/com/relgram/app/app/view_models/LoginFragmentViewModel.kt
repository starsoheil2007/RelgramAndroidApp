package com.relgram.app.app.view_models

import com.relgram.app.view_models.BaseViewModel

class LoginFragmentViewModel : BaseViewModel() {


    var mainText: String? = null

    fun bind(mainText: String?) {
        this.mainText = mainText
    }
}