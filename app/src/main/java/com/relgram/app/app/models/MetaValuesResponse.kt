package com.relgram.app.app.models

/**
 * get meta data of selected by user (user/get_meta_values/) rest
 *
 * @property myMetaData list of my meta data values selected
 * @property resultListOpn list of opn meta data values selected
 */
data class MetaValuesResponse(var myMetaData: List<MyMetaData>, var resultListOpn: List<MyMetaData>)

/**
 * Meta values details
 *
 * @property id id of value
 * @property metaTitle title of meta
 * @property metaType type of value
 * @property metaValueTitle title of value
 * @property metaValueSelected title of value selected
 * @property metaId id of meta
 * @property metaValueId id of meta value in server db
 */
data class MyMetaData(var id: Long, var metaTitle: String, var metaType: Int, var metaValueTitle: String, var metaValueSelected: String, var metaId: Int, var metaValueId: Int)