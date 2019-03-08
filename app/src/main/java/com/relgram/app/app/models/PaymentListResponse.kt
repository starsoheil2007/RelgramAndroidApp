package com.relgram.app.app.models

/**
 * List of pricing list
 *
 * @property id id for send new payment
 * @property title title for pricing option
 * @property price price
 * @property addExpireDay how day added to expire day after buy this pricing
 */
data class PaymentListResponse(var id: Int, var title: String, var price: String, var addExpireDay: String)