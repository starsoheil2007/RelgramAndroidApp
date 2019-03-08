package com.relgram.app.app.models

/**
 * Response of ads/get_banner_ads rest
 * advertising banner images
 *
 * @property id id of banner
 * @property name title of banner
 * @property bannerImage end url of banner (must be concat of base url)
 */
data class BannerResponse(var id: Int, var name: String, var bannerImage: String)