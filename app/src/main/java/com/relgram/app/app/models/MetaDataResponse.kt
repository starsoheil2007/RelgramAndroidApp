package com.relgram.app.app.models

/**
 * Get meta data (Features of people) response (user/get_meta_data and user/get_meta_data_opponent) rest
 *
 * @property id meta data id (use for change or delete)
 * @property title title of meta data
 * @property typeField type for show field in android ui
 * @property sex show for witch sex
 * @property values value/values for show in ui
 */
data class MetaDataResponse(var id: Int, var title: String, var typeField: String, var sex: Boolean, var values: List<MetaValues>)

/**
 * values can be accepted or inserted by user
 *
 * @property id id of value
 * @property title title of value
 */
data class MetaValues(var id: Int, var title: String)

