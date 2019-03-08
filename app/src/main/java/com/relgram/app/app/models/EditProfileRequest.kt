package com.relgram.app.app.models

/**
 * Edit profile request for send to server
 *
 * @property firstName new first name
 * @property lastName new last name
 * @property cityId new city id
 * @property email new email
 * @property birthDate new birth date (dd-mm-yyyy)
 * @property aboutMe new about me text
 * @property aboutSpouse new about my spouse text
 * @property sex new sex (true:Man/false:woman)
 */
data class EditProfileRequest(var firstName: String, var lastName: String, var cityId: Int, var email: String, var birthDate: String, var aboutMe: String, var aboutSpouse: String, var sex: Boolean)