package com.relgram.app.app.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * User Info Table
 * This table has only one row because app is one rows
 * @param id:primary key
 * @param name:Name of user
 * @param token:Token of server
 * @param mobile: Mobile Number of user
 * @param sex: Sex of user
 * @param avatarUrl: The url of avatar from server will be load
 * @param serverId: The user id in server
 */
@Entity(tableName = "user_info")
data class UserInfo(@PrimaryKey(autoGenerate = true) var id: Int? = null, var name: String? = null, var token: String? = null, var mobile: String? = null, var sex: Boolean = true, var avatarUrl: String? = null, var serverId: Long? = null) {}