package com.relgram.app.app.models

/**
 * get payment url from server response (user/get_payment/) rest
 *
 * @property Id  id of payment (tracking number)
 * @property status status of payment
 * @property url url can be opened in browser for process user payment
 */
data class GetPaymentResponse(var Id: String, var status: String, var url: String)