package com.relgram.app.app.view_models

import com.relgram.app.app.models.GalleryImages
import com.relgram.app.app.models.ImageResponse
import com.relgram.app.app.webservice.WebService
import com.relgram.app.view_models.BaseViewModel

/**
 * Gallery Fragment View Model
 * Each user can be image between 0 to 5
 *
 */
class GalleryFragmentViewModel : BaseViewModel() {

    var avatar: String? = null
    var images: List<GalleryImages>? = null

    /**
     * Bind data from server to view
     *
     * @param imageResponse response received from server
     */
    fun bind(imageResponse: ImageResponse) {
        this.avatar = imageResponse.avatar
        this.images = imageResponse.gallery
    }

    /**
     * get full url of avatar
     *
     * @return String url
     */
    fun getFullAvatarUrl(): String {
        return WebService.PICTURE_URL + avatar
    }


    /**
     * get image one from gallery if exist
     *
     * @return String url
     */
    fun getFullImage1Url(): String {
        if (images != null && images!!.isNotEmpty()) {
            return WebService.PICTURE_URL + images!![0].imageUrl
        }
        return ""
    }

    /**
     * get image two from gallery if exist
     *
     * @return String url
     */
    fun getFullImage2Url(): String {
        if (images != null && images!!.size > 1) {
            return WebService.PICTURE_URL + images!![1].imageUrl
        }
        return ""
    }

    /**
     * get image three from gallery if exist
     *
     * @return String url
     */
    fun getFullImage3Url(): String {
        if (images != null && images!!.size > 2) {
            return WebService.PICTURE_URL + images!![2].imageUrl
        }
        return ""
    }

    /**
     * get image four from gallery if exist
     *
     * @return String url
     */
    fun getFullImage4Url(): String {
        if (images != null && images!!.size > 3) {
            return WebService.PICTURE_URL + images!![3].imageUrl
        }
        return ""
    }

    /**
     * get image five from gallery if exist
     *
     * @return String url
     */
    fun getFullImage5Url(): String {
        if (images != null && images!!.size > 4) {
            return WebService.PICTURE_URL + images!![4].imageUrl
        }
        return ""
    }


    /**
     * get image one id for if want call delete request
     *
     * @return long id
     */
    fun getFullImage1Id(): Long {
        if (images != null && images!!.size > 0) {
            return images!![0].id
        }
        return 0
    }

    /**
     * get image two id for if want call delete request
     *
     * @return long id
     */
    fun getFullImage2Id(): Long {
        if (images != null && images!!.size > 1) {
            return images!![1].id
        }
        return 0
    }

    /**
     * get image three id for if want call delete request
     *
     * @return long id
     */
    fun getFullImage3Id(): Long {
        if (images != null && images!!.size > 2) {
            return images!![2].id
        }
        return 0
    }

    /**
     * get image four id for if want call delete request
     *
     * @return long id
     */
    fun getFullImage4Id(): Long {
        if (images != null && images!!.size > 3) {
            return images!![3].id
        }
        return 0
    }

    /**
     * get image five id for if want call delete request
     *
     * @return long id
     */
    fun getFullImage5Id(): Long {
        if (images != null && images!!.size > 4) {
            return images!![4].id
        }
        return 0
    }


}