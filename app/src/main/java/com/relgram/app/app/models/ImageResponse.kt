package com.relgram.app.app.models

/**
 * the image response of user (gallery)
 *
 * @property avatar avatar end url
 * @property gallery list of gallery end url
 */
data class ImageResponse(var avatar: String, var gallery: List<GalleryImages>?)

/**
 * the gallery image items
 *
 * @property id id of image in server (use for delete item)
 * @property imageUrl thee end url of image
 */
data class GalleryImages(var id: Long, var imageUrl: String)