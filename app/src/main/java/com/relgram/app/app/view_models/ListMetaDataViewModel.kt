package com.relgram.app.app.view_models

import com.relgram.app.app.models.MetaDataResponse
import com.relgram.app.view_models.BaseViewModel

class ListMetaDataViewModel : BaseViewModel() {

    var meta: MetaDataResponse? = null

    /**
     * bind meta data to view model
     *
     * @param meta instance of MetaDataResponse
     */
    fun bind(meta: MetaDataResponse) {
        this.meta = meta
    }
}