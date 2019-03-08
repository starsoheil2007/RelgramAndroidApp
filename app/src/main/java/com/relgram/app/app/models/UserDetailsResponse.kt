package com.relgram.app.app.models

/**
 * get other user (not me) details from sever
 *
 * @property userId id of user
 * @property userFirstName first name of user
 * @property userAvatar avatar end url
 * @property userProvince province's name of user
 * @property userCity city's name of user
 * @property userAge age of user
 */
data class UserDetailsResponse(var userId: Long, var userFirstName: String, var userAvatar: String, var userProvince: String, var userCity: String, var userAge: String)