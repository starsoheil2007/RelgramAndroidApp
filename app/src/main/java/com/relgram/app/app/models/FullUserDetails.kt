package com.relgram.app.app.models

/**
 * get user details in login or register
 *
 * @property userId id in sever
 * @property firstName first name
 * @property lastName last name
 * @property sex sex (true:Man/false:woman)
 * @property age Age of user
 * @property joinTime join time to app
 * @property city city name
 * @property province province name
 * @property activated user is activate or blocked
 * @property aboutMe about me text
 * @property aboutSpouse about my spouse text
 * @property avatar end url of avatar
 * @property images list of images
 * @property expireDate date of expire membership
 * @property chatCount count of chats
 * @property flowerCount count of users flower
 * @property flowingCount count of flowing users
 * @property birthDate date of birth
 * @property cityId id of city in server
 * @property provinceId id of province in server
 * @property email email address
 */
data class FullUserDetails(var userId: Long?, var firstName: String, var lastName: String, var sex: Boolean, var age: Int, var joinTime: String, var city: String, var province: String, var activated: Boolean?, var aboutMe: String?, var aboutSpouse: String?, var avatar: String?, var images: List<String>, var expireDate: String?, var chatCount: Int, var flowerCount: Int, var flowingCount: Int, var birthDate: String?, var cityId: Int, var provinceId: Int, var email: String?)
