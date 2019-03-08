package com.relgram.app.app.view_models

import com.relgram.app.app.HamsanApp
import com.relgram.app.app.R
import com.relgram.app.app.models.PaymentListResponse
import com.relgram.app.view_models.BaseViewModel

class ListPaymentViewModel : BaseViewModel() {

    var id: Int? = null
    var title: String? = null
    var price: String? = null
    var add_expire_day: String? = null

    fun bind(paymentListResponse: PaymentListResponse) {
        id = paymentListResponse.id
        title = paymentListResponse.title
        price = paymentListResponse.price
        add_expire_day = paymentListResponse.addExpireDay

    }

    fun getFullPrice(): String {
        return "$price " + HamsanApp.context.resources.getString(R.string.toman)
    }


}