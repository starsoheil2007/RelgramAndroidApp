package com.relgram.app.app.models

/**
 * List of my favorites or users favorite me (user/get_my_fav_user, user/get_my_faved_user) rest
 *
 * @property pk id in server
 * @property userFirstName first name of user
 * @property userAvatar user avatar end url
 * @property age age of user
 * @property userCity city of user
 * @property userProvince province of user
 * @property userId id of user (use for show profile)
 */
data class MyFavUser(var pk: Long, var userFirstName: String, var userAvatar: String, var age: Int, var userCity: String, var userProvince: String, var userId: Long);
