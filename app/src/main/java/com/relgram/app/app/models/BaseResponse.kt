package com.relgram.app.app.models

import com.google.gson.annotations.SerializedName

/**
 * Base Response of all rest response
 *
 * this class show 4 parameters
 * if issucess is true show request has been successfully completed
 * if not error code show errors (based on rest document)
 * item if your request is one object
 * listItems if your request is list of one object
 * @param T type of responses
 */
class BaseResponse<T> {

    @SerializedName("IsSuccess")
    var isSuccess: Boolean = false

    @SerializedName("Item")
    var item: T? = null

    @SerializedName("ListItems")
    var listItems: List<T>? = null

    @SerializedName("ErrorCode")
    var errorCode: Int? = null

}