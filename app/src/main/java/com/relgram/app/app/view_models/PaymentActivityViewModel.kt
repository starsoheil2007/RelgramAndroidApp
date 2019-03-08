package com.relgram.app.app.view_models

import com.relgram.app.app.models.PaymentListResponse
import com.relgram.app.view_models.BaseViewModel

/**
 * List of pricing List
 *
 */
class PaymentActivityViewModel : BaseViewModel() {

    lateinit var listOfPayment: List<PaymentListResponse>

    /**
     * get list of pricing List
     *
     * @param listOfPayment instance PaymentListResponse
     */
    fun bind(listOfPayment: List<PaymentListResponse>) {
        this.listOfPayment = listOfPayment
    }
}