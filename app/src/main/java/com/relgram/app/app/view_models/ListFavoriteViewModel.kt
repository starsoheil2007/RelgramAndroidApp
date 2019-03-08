package com.relgram.app.app.view_models

import com.relgram.app.app.HamsanApp
import com.relgram.app.app.R
import com.relgram.app.app.models.MyFavUser
import com.relgram.app.app.webservice.WebService
import com.relgram.app.view_models.BaseViewModel

/**
 * Favorite List binding view model
 *
 */
class ListFavoriteViewModel : BaseViewModel() {

    var pk: Long = 0
    var userFirstName: String? = null
    var userAvatar: String? = null
    var age: Int? = null
    var userCity: String? = null
    var userProvince: String? = null

    /**
     * bind data to view
     *
     * @param favUser instance of favorite user
     */
    fun bind(favUser: MyFavUser) {
        pk = favUser.pk
        userFirstName = favUser.userFirstName
        userAvatar = favUser.userAvatar
        age = favUser.age
        userCity = favUser.userCity
        userProvince = favUser.userProvince
    }

    /**
     * get full avatar url
     *
     * @return full avatar url
     */
    fun getFullAvatarUrl(): String {
        return WebService.PICTURE_URL + userAvatar
    }

    /**
     * get full user age
     *
     * @return string of age
     */
    fun getFullUserAge(): String {
        return age.toString() + " " + HamsanApp.context.resources.getString(R.string.sale)
    }

    /**
     * get Full Location of user (province/city)
     *
     * @return string of
     */
    fun getFullLocation(): String {
        return "$userProvince $userCity"
    }

    /**
     * get fav id for delete request
     *
     * @return id of fav item as long
     */
    fun getDeleteId(): Long {
        return pk
    }
}