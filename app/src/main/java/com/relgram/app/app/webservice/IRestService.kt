package com.relgram.app.app.webservice

import com.relgram.app.app.models.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface IRestService {

    /**
     * send request to server for send activation code for user by sms
     *
     * @param mobile mobile number by format (09********)
     * @return base response check isSuccess
     */
    @POST("user/get_activation_code/")
    @FormUrlEncoded
    fun getActivationCode(@Field("mobile") mobile: String): Observable<BaseResponse<Any>>

    /**
     * check mobile number is already user or not
     *
     * @param mobile mobile number by format (09********)
     * @return base response check isSuccess && errorCode
     */
    @GET("user/exist_mobile_number/")
    fun existMobileNumber(@Query("mobile") mobile: String): Observable<BaseResponse<Any>>

    /**
     * check entered code is successful or not before register
     *
     * @param mobile mobile number by format (09********)
     * @param code 6 digit code
     *
     * @return base response check isSuccess && errorCode
     */
    @POST("user/check_activation_code/")
    @FormUrlEncoded
    fun checkActivationCode(@Field("mobile") mobile: String, @Field("code") code: String): Observable<BaseResponse<Any>>

    /**
     * register user to server
     *
     * @param mobile mobile number
     * @param code activision code that send by sms
     * @param firstName user first name
     * @param lastName user last name
     * @param sex sex of user (true:Man/false:woman)
     * @param birthDate birth date (dd-mm-yyyy)
     * @param cityId id of city selected
     * @param email email of user
     * @param aboutMe about me text
     * @param aboutSpouse about my spouse text
     * @param avatar the file of avatar that selected from gallery or camera
     *
     * @return BaseResponse<UserRegisterResponse>
     */
    @POST("user/register_user/")
    @Multipart
    fun registerUser(@Part("mobile") mobile: RequestBody, @Part("code") code: RequestBody, @Part("firstName") firstName: RequestBody, @Part("lastName") lastName: RequestBody, @Part("sex") sex: Boolean, @Part("birthDate") birthDate: RequestBody, @Part("cityId") cityId: Int, @Part("email") email: RequestBody, @Part("aboutMe") aboutMe: RequestBody, @Part("aboutSpouse") aboutSpouse: RequestBody, @Part avatar: MultipartBody.Part?): Observable<BaseResponse<UserRegisterResponse>>

    /**
     * get meta values of user
     *
     * @param userId server id of user
     *
     * @return BaseResponse<MetaValuesResponse>
     */
    @GET("user/get_meta_values/")
    fun getMetaValues(@Query("userId") userId: Long?): Observable<BaseResponse<MetaValuesResponse>>


    /**
     * get List of Last user by pagination
     *
     * @param page page of your selection
     * @return BaseResponse<UserDetailsResponse>
     */
    @GET("user/get_last_user/")
    fun getLastUser(@Query("page") page: Int): Observable<BaseResponse<UserDetailsResponse>>

    /**
     * search user in one city
     *
     * @param page page for pagination list view
     * @param cityId id of city search on it
     *
     * @return BaseResponse<UserDetailsResponse>
     */
    @GET("user/search/")
    fun search(@Query("page") page: Int, @Query("cityId") cityId: Int): Observable<BaseResponse<UserDetailsResponse>>


    /**
     * get other user details for show in user profile
     *
     * @param userId id of user
     * @return BaseResponse<FullUserDetails>>
     */
    @POST("user/get_user_info_for_show/")
    @FormUrlEncoded
    fun getUserInfoForShow(@Field("userId") userId: Long): Observable<BaseResponse<FullUserDetails>>

    /**
     * get my info from server (in splash) for chane in other devices
     *
     * @return BaseResponse<FullUserDetails>>
     */
    @POST("user/get_my_info/")
    fun getMyInfo(): Observable<BaseResponse<FullUserDetails>>


    /**
     * send meta values to server
     *
     * @param meValues json string of my meta values
     * @param opnValues json string of my spouse
     *
     * @return base response check isSuccess
     */
    @POST("user/add_meta_values/")
    @FormUrlEncoded
    fun addMetaValues(@Field("mevalues") meValues: String, @Field("opnvalues") opnValues: String): Observable<BaseResponse<Any>>


    /**
     * Update user device info & FCN info for chart and send notification in server
     *
     * @param fcmId the firebase id of device
     * @param phoneId the imei or phone id
     * @param deviceType type of app (android/ios)
     */
    @POST("user/update_details/")
    @FormUrlEncoded
    fun updateUserDetails(@Field("fcmId") fcmId: String, @Field("phoneId") phoneId: String, @Field("deviceType") deviceType: String): Observable<BaseResponse<Any>>

    /**
     * login user to server
     *
     * @param mobile mobile number
     * @param code activision code that send by sms
     */
    @POST("user/login_user/")
    @FormUrlEncoded
    fun loginUser(@Field("mobile") mobile: String, @Field("code") code: String): Observable<BaseResponse<UserRegisterResponse>>

    /**
     * edit user request
     *
     * @param firstName new first name
     * @param lastName new last name
     * @param cityId new city id
     * @param email new email
     * @param birthDate new birth date (dd-mm-yyyy)
     * @param aboutMe new about me text
     * @param aboutSpouse new about my spouse text
     * @param sex new sex (true:Man/false:woman)
     *
     * @return BaseResponse<UserRegisterResponse>
     */
    @POST("user/edit_user_profile/")
    @FormUrlEncoded
    fun editUser(@Field("firstName") firstName: String, @Field("lastName") lastName: String, @Field("cityId") cityId: Int, @Field("email") email: String, @Field("birthDate") birthDate: String, @Field("aboutMe") aboutMe: String, @Field("aboutSpouse") aboutSpouse: String, @Field("sex") sex: Boolean): Observable<BaseResponse<UserRegisterResponse>>

    /**
     * get list of province
     *
     * @return BaseResponse<ProvinceCityResponse>
     */
    @GET("user/get_province_list")
    fun getProvinceList(): Observable<BaseResponse<ProvinceCityResponse>>

    /**
     * get list of city from one province
     *
     * @param provinceId id of province
     *
     * @return BaseResponse<ProvinceCityResponse>>
     */
    @GET("user/get_city_list")
    fun getCityList(@Query("provinceId") provinceId: Int): Observable<BaseResponse<ProvinceCityResponse>>

    /**
     * upload an image for gallery
     *
     * @param image file of image from phone
     * @return BaseResponse check isSuccess
     */
    @POST("user/upload/")
    @Multipart
    fun uploadImage(@Part image: MultipartBody.Part?): Observable<BaseResponse<Any>>

    /**
     * upload avatar to server
     *
     * @param image file of image from phone
     * @return BaseResponse check isSuccess
     */
    @POST("user/upload_avatar/")
    @Multipart
    fun uploadAvatar(@Part image: MultipartBody.Part?): Observable<BaseResponse<Any>>

    /**
     * get My Meta data
     *
     * @return BaseResponse<MetaDataResponse>
     */
    @GET("user/get_meta_data")
    fun getMetaData(): Observable<BaseResponse<MetaDataResponse>>


    /**
     * get my Opponent meta data
     *
     * @return BaseResponse<MetaDataResponse>
     */
    @GET("user/get_meta_data_opponent")
    fun getMetaDataOpponent(): Observable<BaseResponse<MetaDataResponse>>

    /**
     * get my gallery list
     *
     * @return BaseResponse<ImageResponse>>
     */
    @GET("user/get_my_gallery")
    fun getMyGalley(): Observable<BaseResponse<ImageResponse>>


    /**
     * Start new chat service
     *
     * @param userId witch user you want start chat
     * @return BaseResponse<StartChatResponse>
     */
    @POST("user/start_chat/")
    @FormUrlEncoded
    fun startChat(@Field("userId") userId: Long): Observable<BaseResponse<StartChatResponse>>

    /**
     * get chat details
     *
     * @param chatId chat server id
     * @param page page for pagination list view
     *
     * @return BaseResponse<ChatGetResponse>
     */
    @GET("user/chat")
    fun getChat(@Query("chatId") chatId: Long, @Query("page") page: Int): Observable<BaseResponse<ChatGetResponse>>

    /**
     * send new message
     * only one of message or image can be filled
     * @param chatId conversion id
     * @param message test message
     * @param image image file
     *
     * @return BaseResponse<StartChatResponse>
     */
    @POST("user/chat/")
    @Multipart
    fun sendChat(@Part("chatId") chatId: Long, @Part("message") message: RequestBody?, @Part avatar: MultipartBody.Part?): Observable<BaseResponse<StartChatResponse>>

    /**
     * Get all chat history
     *
     * @param page page for pagination list view
     *
     * @return BaseResponse<ChatHistoryResponse>
     */
    @GET("user/chat_history")
    fun chatHistory(@Query("page") page: Int): Observable<BaseResponse<ChatHistoryResponse>>

    /**
     * Add user to my favorites
     *
     * @param userId id of user
     *
     * @return Base Response (check status)
     */
    @POST("user/user_fav/")
    @FormUrlEncoded
    fun userFav(@Field("userId") userId: Long): Observable<BaseResponse<Any>>

    /**
     * Delete user from my favorites
     *
     * @param favId favorite id
     *
     * @return BaseResponse<Any>
     */
    @DELETE("user/user_fav/")
    fun deleteUserFav(@Query("favId") favId: Long): Observable<BaseResponse<Any>>

    /**
     * delete image from gallery
     *
     * @param id id of image
     *
     * @return base response check isSuccess
     */
    @DELETE("user/delete_gallery/")
    fun deleteGallery(@Query("id") id: Long): Observable<BaseResponse<Any>>

    /**
     * get pricing list
     *
     * @return BaseResponse<PaymentListResponse>
     */
    @GET("user/get_payment_list")
    fun getPaymentList(): Observable<BaseResponse<PaymentListResponse>>

    /**
     * Delete account and all history
     *
     * @return BaseResponse<Any>
     */
    @POST("user/delete_account/")
    fun deleteAccount(): Observable<BaseResponse<Any>>

    /**
     * start new payment
     *
     * @param paymentId id of pricing list
     *
     * @return BaseResponse<GetPaymentResponse>>
     */
    @POST("user/get_payment/")
    @FormUrlEncoded
    fun getPayment(@Field("paymentId") paymentId: Int): Observable<BaseResponse<GetPaymentResponse>>

    /**
     * get list of my favorites
     *
     * @param page page for pagination list view
     *
     * @return BaseResponse<MyFavUser>
     */
    @GET("user/get_my_fav_user")
    fun getMyUserFav(@Query("page") page: Int): Observable<BaseResponse<MyFavUser>>

    /**
     * get List of user Favorites me
     *
     * @param page page for pagination list view
     *
     * @return BaseResponse<MyFavUser>
     */
    @GET("user/get_my_faved_user")
    fun getUserFavMe(@Query("page") page: Int): Observable<BaseResponse<MyFavUser>>

    /**
     * get older tickets
     *
     * @param page page for pagination list view
     *
     * @return BaseResponse<ContactUsResponse>
     */
    @GET("user/connect_us")
    fun getConnectUs(@Query("page") page: Int): Observable<BaseResponse<ContactUsResponse>>

    /**
     * send new ticket
     *
     * @param title title of ticket
     * @param message message of ticket (description)
     * @param type type of ticket (read server document for ticket
     *
     * @return BaseResponse<ContactUsResponse>
     */
    @POST("user/connect_us/")
    @FormUrlEncoded
    fun sendConnectUs(@Field("title") title: String, @Field("text") message: String, @Field("type") type: String): Observable<BaseResponse<ContactUsResponse>>

    /**
     * get app settings list from server
     *
     * @return BaseResponse<SettingResponse>
     */
    @GET("user/get_settings_list")
    fun getSettings(): Observable<BaseResponse<SettingResponse>>

    /**
     * get advertising banner images
     *
     * @return BaseResponse<BannerResponse>
     */
    @GET("ads/get_banner_ads")
    fun getBannerAds(): Observable<BaseResponse<BannerResponse>>

    /**
     * get latest version of app has been released
     *
     * @return BaseResponse<AppUpdateResponse>
     */
    @GET("ads/check_last_version/")
    fun checkAppUpdate(): Observable<BaseResponse<AppUpdateResponse>>


    /**
     * block user chat
     *
     * @param chatId id of chat
     *
     * @return response check isSuccess
     */
    @POST("user/block_chat/")
    @FormUrlEncoded
    fun blockChat(@Field("chatId") chatId: Long): Observable<BaseResponse<Any>>

    /**
     * unblock Chat
     *
     * @param chatId id of chat
     *
     * @return response check isSuccess
     */
    @POST("user/unblock_chat/")
    @FormUrlEncoded
    fun unBlockChat(@Field("chatId") chatId: Long): Observable<BaseResponse<Any>>


}