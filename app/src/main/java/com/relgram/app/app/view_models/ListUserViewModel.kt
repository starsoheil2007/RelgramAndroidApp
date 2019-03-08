package com.relgram.app.app.view_models

import android.content.Context
import android.content.Intent
import android.view.View
import com.relgram.app.app.HamsanApp
import com.relgram.app.app.R
import com.relgram.app.app.models.UserDetailsResponse
import com.relgram.app.app.view.activities.UserProfileViewActivity
import com.relgram.app.app.webservice.WebService
import com.relgram.app.view_models.BaseViewModel

class ListUserViewModel : BaseViewModel() {

    var userId: Long? = null
    var userFirstName: String? = null
    var userAvatar: String? = null
    var userProvince: String? = null
    var userCity: String? = null
    var userAge: String? = null
    var context: Context? = null


    fun bind(onUser: UserDetailsResponse, context: Context) {
        userId = onUser.userId
        userFirstName = onUser.userFirstName
        userAvatar = onUser.userAvatar
        userProvince = onUser.userProvince
        userCity = onUser.userCity
        userAge = onUser.userAge
        this.context = context
    }

    /**
     * get user avatar image full url
     *
     * @return string
     */
    fun getFullAvatarUrl(): String {
        return WebService.PICTURE_URL + userAvatar
    }

    /**
     * get full user age
     *
     * @return String
     */
    fun getFullUserAge(): String {
        return userAge.toString() + " " + HamsanApp.context.resources.getString(R.string.sale)
    }


    /**
     * get full location
     *
     * @return String (province city)
     */
    fun getFullLocation(): String {
        return "$userProvince $userCity"
    }

    /**
     * show user info activity by clicking view
     * send user id as intent extra to show this user info
     *
     * @param v view that clicked
     */
    fun onClickInfo(v: View) {
        val intent = Intent(context, UserProfileViewActivity::class.java)
        intent.putExtra(UserProfileViewActivity.USER_ID, userId)
        context!!.startActivity(intent)
    }
}