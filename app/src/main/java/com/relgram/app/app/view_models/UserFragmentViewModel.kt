package com.relgram.app.app.view_models

import android.arch.lifecycle.MutableLiveData
import android.view.View
import android.widget.AdapterView
import com.mojtaba.materialdatetimepicker.utils.PersianCalendar
import com.relgram.app.app.HamsanApp
import com.relgram.app.app.R
import com.relgram.app.app.models.FullUserDetails
import com.relgram.app.app.webservice.WebService
import com.relgram.app.view_models.BaseViewModel

/**
 * User profile fragment
 *
 */
class UserFragmentViewModel : BaseViewModel() {

    public lateinit var onProvinceSelectedListener: RegisterFragmentViewModel.onProvinceSelected
    public lateinit var onCitySelectedListener: RegisterFragmentViewModel.onCitySelected


    fun onSelectItem(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        onProvinceSelectedListener.onSelected(id)
    }

    fun onCitySelectItem(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        onCitySelectedListener.onSelected(id)
    }

    var firstName: MutableLiveData<String> = MutableLiveData()
    var lastName: MutableLiveData<String> = MutableLiveData()
    var sex: Boolean? = true
    var age: Int? = 0
    lateinit var joinTime: String
    lateinit var city: String
    lateinit var province: String
    var activated: Boolean? = true
    var aboutMe: String? = null
    var aboutSpouse: String? = null
    var avatar: String? = null
    var images: List<String>? = null
    var expireDate: String? = null
    var chatCount: Int? = 0
    var flowerCount: Int? = 0
    var flowingCount: Int? = 0
    var cityId: Int? = 0
    var provinceId: Int? = 0
    var birthDate: String? = null
    var email: String? = null


    /**
     * bind function
     *
     * @param userDetais instance of FullUserDetails
     */
    fun bind(userDetais: FullUserDetails) {
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
        birthDate = userDetais.birthDate
        provinceId = userDetais.provinceId
        cityId = userDetais.cityId
        email = userDetais.email

    }


    /**
     * get User Expire Date for show in view
     *
     * @return String
     */
    fun getExpireDateText(): String {
        if (expireDate == null) {
            return HamsanApp.context.getText(R.string.notActivate)
                    .toString()
        } else {
            return HamsanApp.context.getText(R.string.expireDate).toString() + " " + expireDate
        }
    }

    /**
     * get User Chat Count for show in view
     *
     * @return String
     */
    fun getChatCountText(): String {
        if (chatCount != null) return chatCount.toString()
        return "0"
    }

    /**
     * get User Follower Count for show in view
     *
     * @return String
     */
    fun getFlowerCountText(): String {
        if (flowerCount != null) return flowerCount.toString()
        return "0"
    }

    /**
     * get User Following Count for show in view
     *
     * @return String
     */
    fun getFlowingCountText(): String {
        if (flowingCount != null) return flowingCount.toString()
        return "0"
    }

    /**
     * get User full avatar for show in view
     *
     * @return String
     */
    fun getFullAvatarUrl(): String {
        return WebService.PICTURE_URL + avatar
    }

    /**
     * get User full name for show in view
     *
     * @return String
     */
    fun getFullName(): String {
        return "${firstName.value} ${lastName.value}"
    }

    /**
     * get User sex for show in view
     *
     * @return Boolean
     */
    fun getSexVar(): Boolean {
        if (this.sex!!) return true
        return false
    }

    /**
     * convert birth date from server to persian date
     *
     * @return String
     */
    fun getPersianBirthDate(): String {
        if (birthDate != null) {
            val now = PersianCalendar()
            val spDate = birthDate!!.split("-")
            now.set(spDate[0].toInt(), spDate[1].toInt(), spDate[2].toInt())
            return now.persianYear.toString() + "/" + now.persianMonth.toString() + "/" + now.persianDay.toString()
        }
        return ""
    }


    interface onProvinceSelected {
        fun onSelected(id: Long)
    }


    interface onCitySelected {
        fun onSelected(id: Long)
    }
}