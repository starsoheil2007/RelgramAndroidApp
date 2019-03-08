package com.relgram.app.app.models

/**
 * the app settings received from server
 *
 * @property id id of settings (not important)
 * @property name name of settings
 * @property value value of settings
 */
data class SettingResponse(var id: Long, var name: String, var value: String)