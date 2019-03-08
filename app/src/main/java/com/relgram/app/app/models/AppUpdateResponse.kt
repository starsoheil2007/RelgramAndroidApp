package com.relgram.app.app.models

/**
 * Response of ads/check_last_version/ service
 *
 * @property versionCode version code of last version
 * @property versionName version name of last version
 * @property forceUpdate need to be update to last version is force or not
 * @property change change list text to show for user
 * @property downloadLink download link of last version apk
 */
data class AppUpdateResponse(var versionCode: Int, var versionName: String, var forceUpdate: Boolean, var change: String, var downloadLink: String)