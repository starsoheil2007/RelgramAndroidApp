package com.relgram.app.app.models

import java.io.File
import java.io.Serializable

/**
 * request of register for move between activity
 *
 * @property mobile mobile number
 * @property code activision code that send by sms
 * @property firstName user first name
 * @property lastName user last name
 * @property sex sex of user (true:Man/false:woman)
 * @property birthDate birth date (dd-mm-yyyy)
 * @property cityId id of city selected
 * @property email email of user
 * @property aboutMe about me text
 * @property aboutSpouse about my spouse text
 * @property avatar the file of avatar that selected from gallery or camera
 */
data class RegisterRequest(var mobile: String, var code: String, var firstName: String, var lastName: String, var sex: Boolean, var birthDate: String?, var cityId: Int, var email: String, var aboutMe: String, var aboutSpouse: String, var avatar: File?) : Serializable