package com.relgram.app.app.view_models

import com.relgram.app.app.HamsanApp
import com.relgram.app.app.R
import com.relgram.app.view_models.BaseViewModel

/**
 * Info (About us) Activity view Model
 *
 */
class InfoActivityViewModel : BaseViewModel() {


    var version: String? = null

    /**
     * bin version of app to show in activity
     *
     * @param version string version name
     */
    fun bind(version: String?) {
        this.version = version
    }

    /**
     * get full version text for show in view
     *
     * @return string text
     */
    fun getFullVersion(): String {
        return HamsanApp.context.resources.getString(R.string.version) + " " + version
    }


}