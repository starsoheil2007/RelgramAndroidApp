package com.relgram.app.app.models

/**
 * register successful response from server
 *
 * @property token token of user for put in header of all other rest
 * @property userId id of user in server
 * @property firstName first name of user
 * @property lastName last name of user
 * @property sex sex (true:Man/false:woman)
 * @property birthDate  birth date (dd-mm-yyyy)
 * @property joinTime time of user joined to server
 * @property city city name
 * @property cityId city id
 * @property province province name
 * @property email email
 * @property activated user is activated
 * @property avatar avatar url of user
 * @property images image gallery uploaded
 */
data class UserRegisterResponse(var token: String, var userId: Long, var firstName: String, var lastName: String, var sex: Boolean, var birthDate: String, var joinTime: String, var city: String, var cityId: Int, var province: String, var email: String, var activated: Boolean, var avatar: String, var images: List<String>)