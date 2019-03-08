package com.relgram.app.app.view_models

import android.content.Context
import com.relgram.app.app.R
import com.relgram.app.view_models.BaseViewModel

/**
 * update Activity view model
 *
 */
class UpdateActivityViewModel : BaseViewModel() {


    var versionCode: Int? = null
    var versionName: String? = null
    var oldVersionName: String? = null
    var forceUpdate: Boolean? = null
    var change: String? = null
    var downloadLink: String? = null
    var context: Context? = null

    /**
     * bind function to view
     *
     * @param versionCode latest version code from server
     * @param versionName latest version name from server
     * @param oldVersionName installed app version name
     * @param forceUpdate is force to update
     * @param change change list of latest version
     * @param downloadLink download link of latest version (apk)
     * @param context android context
     */
    fun bind(versionCode: Int?, versionName: String?, oldVersionName: String?, forceUpdate: Boolean?, change: String?, downloadLink: String?, context: Context) {
        this.versionCode = versionCode
        this.versionName = versionName
        this.oldVersionName = oldVersionName
        this.forceUpdate = forceUpdate
        this.change = change
        this.downloadLink = downloadLink
        this.context = context
    }

    /**
     * get version code of installed app
     *
     * @return String
     */
    fun getYourVersion(): String {
        return context!!.resources.getString(R.string.yourVersion) + oldVersionName
    }

    /**
     * get latest version from server
     *
     * @return String
     */
    fun getNewVersion(): String {
        return context!!.resources.getString(R.string.newVersion) + versionName
    }
}