package com.relgram.app.app.view_models

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.view.View
import com.google.gson.Gson
import com.relgram.app.app.HamsanApp
import com.relgram.app.app.R
import com.relgram.app.app.library.Toaster
import com.relgram.app.app.models.BaseResponse
import com.relgram.app.app.models.FullUserDetails
import com.relgram.app.app.view.activities.ChargeNeedActivity
import com.relgram.app.app.view.activities.ChatMessagingActivity
import com.relgram.app.app.view.activities.OtherUserMetaDataActivity
import com.relgram.app.app.webservice.WebService
import com.relgram.app.view_models.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

/**
 * User Profile Activity View Model
 *
 */
class UserProfileViewActivityViewModel : BaseViewModel() {

    var userId: Long? = null
    var firstName: MutableLiveData<String> = MutableLiveData()
    var lastName: MutableLiveData<String> = MutableLiveData()
    var sex: Boolean? = true
    var age: Int? = 0
    var joinTime: String? = null
    var city: String? = null
    var province: String? = null
    var activated: Boolean? = true
    var aboutMe: String? = null
    var aboutSpouse: String? = null
    var avatar: String? = null
    var images: List<String>? = null
    var expireDate: String? = null
    var chatCount: Int? = 0
    var flowerCount: Int? = 0
    var flowingCount: Int? = 0
    var context: Context? = null


    /**
     * bind data to view model
     *
     * @param userDetais instance of FullUserDetails
     * @param context Context of android
     */
    fun bind(userDetais: FullUserDetails, context: Context) {
        userId = userDetais.userId
        firstName.value = userDetais.firstName
        lastName.value = userDetais.lastName
        sex = userDetais.sex
        age = userDetais.age
        joinTime = userDetais.joinTime
        city = userDetais.city
        province = userDetais.province
        activated = userDetais.activated
        aboutMe = userDetais.aboutMe
        aboutSpouse = userDetais.aboutSpouse
        avatar = userDetais.avatar
        images = userDetais.images
        expireDate = userDetais.expireDate
        chatCount = userDetais.chatCount
        flowerCount = userDetais.flowerCount
        flowingCount = userDetais.flowingCount
        this.context = context

    }

    /**
     * get user avatar image full url
     *
     * @return string
     */
    fun getFullAvatarUrl(): String {
        return WebService.PICTURE_URL + avatar
    }

    /**
     * get full location
     *
     * @return String (province city)
     */
    fun getFullLocation(): String {
        return "$province $city"
    }

    /**
     * get full user age
     *
     * @return String
     */
    fun getFullUserAge(): String {
        return age.toString() + " " + HamsanApp.context.resources.getString(R.string.sale)
    }

    /**
     * add user to my favorite list by clicking add fav button
     * this item call rest api
     *
     * @param v view that clicked
     */
    fun favUser(v: View) {
        val callRest = WebService().userFav(this.userId!!).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    // Progrss Start
                }
                .doOnTerminate {
                    // Progress End
                }
                .subscribe({ result ->
                    if (result.isSuccess && result.item != null) {
                        Toaster.toast(this.context!!, R.string.addtoFavSuccesfull)
                    }
                }, { error ->

                    try {
                        var errorResult = Gson().fromJson((error as HttpException).response().errorBody()!!.string(), BaseResponse::class.java)
                        if (errorResult.errorCode == 101) {
                            Toaster.toast(this.context!!, R.string.beforeAddToFavSuccesfull)
                        } else {
                            //  TODO: Show Error
                        }
                    } catch (e: Exception) {
                        //  TODO: Show Error
                    }

                })
    }

    /**
     * start chat with user by clicking chat button
     * this item call rest api
     *
     * @param v view that clicked
     */
    fun startChat(v: View) {
        val callRest = WebService().startChat(this.userId!!).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    // Progrss Start
                }
                .doOnTerminate {
                    // Progress End
                }
                .subscribe({ result ->
                    if (result.isSuccess && result.item != null) {
                        val intent = Intent(context, ChatMessagingActivity::class.java)
                        intent.putExtra(ChatMessagingActivity.CHAT_ID, result.item!!.chatId)
                        context!!.startActivity(intent)
                    }
                }, { error ->
                    try {
                        var errorResult = Gson().fromJson((error as HttpException).response().errorBody()!!.string(), BaseResponse::class.java)
                        if (errorResult.errorCode == 900) {
                            Toaster.toast(this.context!!, R.string.youNeedExpDate)
                            val intent = Intent(context, ChargeNeedActivity::class.java)
                            context!!.startActivity(intent)
                        } else if (errorResult.errorCode == 901) {
                            Toaster.toast(this.context!!, R.string.youAreNotActive)
                        } else {
                            //  TODO: Show Error
                        }
                    } catch (e: Exception) {
                        //  TODO: Show Error
                    }
                })
    }

    fun showMoreInfo(v: View) {
        var intent = Intent(context, OtherUserMetaDataActivity::class.java)
        intent.putExtra(OtherUserMetaDataActivity.USER_ID, userId)
        context!!.startActivity(intent)
    }
}