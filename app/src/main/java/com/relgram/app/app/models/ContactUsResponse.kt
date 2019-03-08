package com.relgram.app.app.models

/**
 * Tickt for support team results (ser/connect_us)
 *
 * @property pk id of ticket in server
 * @property title title of ticket
 * @property text text of ticket
 * @property time time of ticket
 * @property type type of ticket
 * @property answer admin system answer to ticket (can be empty)
 */
data class ContactUsResponse(var pk: Long, var title: String, var text: String, var time: String, var type: String, var answer: String)