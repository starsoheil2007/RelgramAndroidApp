package com.relgram.app.app.webservice

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.relgram.app.app.BuildConfig
import com.relgram.app.app.database.AppDatabases
import com.relgram.app.app.models.*
import dagger.Module
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * a class to call all Web service
 *
 */
@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
class WebService {

    companion object {
        /**
         * Base url of all picture
         */
        const val PICTURE_URL = "http://relgram.com"
        /**
         * Base url of all rest requests
         */
        private const val BASE_URL = "$PICTURE_URL/api/v1/"

    }

    private var service: IRestService

    /**
     * create retrofit service
     *
     * @return retrofit cutomised instance
     */
    private fun provideRetrofitInterface(): Retrofit {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.connectTimeout(20, TimeUnit.SECONDS)
        httpClient.readTimeout(20, TimeUnit.SECONDS)
        httpClient.writeTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(logging)
        }
        val token = getToken()
        if (token != null) {
            httpClient.addInterceptor { chain ->
                val request = chain.request()
                        .newBuilder()
                request.addHeader("Authorization", "Token $token")
                chain.proceed(request.build())
            }
        }

        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        gsonBuilder.registerTypeAdapter(BaseResponse::class.java, Deserializer<BaseResponse<Any>>())

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }

    /**
     * init the class and retrofit
     */
    init {
        val retrofit = provideRetrofitInterface()
        service = retrofit.create(IRestService::class.java)
    }

    /**
     * get Token of user form database
     *
     * @return string token or null
     */

    private fun getToken(): String? {
        return AppDatabases.getInstance()
                ?.userInfoDao()
                ?.getToken()
    }

    /**
     * send request to server for send activation code for user by sms
     *
     * @param mobile mobile number by format (09********)
     * @return base response check isSuccess
     */
    fun getActivationCode(mobile: String): Observable<BaseResponse<Any>> {
        return service.getActivationCode(mobile)
    }

    /**
     * check mobile number is already user or not
     *
     * @param mobile mobile number by format (09********)
     * @return base response check isSuccess && errorCode
     */
    fun existMobileNumber(mobile: String): Observable<BaseResponse<Any>> {
        return service.existMobileNumber(mobile)
    }

    /**
     * check entered code is successful or not before register
     *
     * @param mobile mobile number by format (09********)
     * @param code 6 digit code
     *
     * @return base response check isSuccess && errorCode
     */
    fun checkActivationCode(mobile: String, code: String): Observable<BaseResponse<Any>> {
        return service.checkActivationCode(mobile, code)
    }

    /**
     * get meta values of user
     *
     * @param userId server id of user
     *
     * @return BaseResponse<MetaValuesResponse>
     */
    fun getMetaValues(userId: Long?): Observable<BaseResponse<MetaValuesResponse>> {
        return service.getMetaValues(userId)
    }

    /**
     * get List of Last user by pagination
     *
     * @param page page of your selection
     * @return BaseResponse<UserDetailsResponse>
     */

    fun getLastUser(page: Int): Observable<BaseResponse<UserDetailsResponse>> {
        return service.getLastUser(page)
    }

    /**
     * get other user details for show in user profile
     *
     * @param userId id of user
     * @return BaseResponse<FullUserDetails>>
     */
    fun getUserInfoForShow(userId: Long): Observable<BaseResponse<FullUserDetails>> {
        return service.getUserInfoForShow(userId)
    }

    /**
     * get my info from server (in splash) for chane in other devices
     *
     * @return BaseResponse<FullUserDetails>>
     */
    fun getMyInfo(): Observable<BaseResponse<FullUserDetails>> {
        return service.getMyInfo()
    }

    /**
     * send meta values to server
     *
     * @param meValues json string of my meta values
     * @param opnValues json string of my spouse
     *
     * @return base response check isSuccess
     */
    fun addMetaValues(meValues: String, opnValues: String): Observable<BaseResponse<Any>> {
        return service.addMetaValues(meValues, opnValues)
    }

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

    fun registerUser(mobile: String, code: String, firstName: String, lastName: String, sex: Boolean, birthDate: String, cityId: Int, email: String, aboutMe: String, aboutSpouse: String, avatar: File?): Observable<BaseResponse<UserRegisterResponse>> {
        var avatarPart: MultipartBody.Part? = null
        if (avatar != null) {
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), avatar)
            avatarPart = MultipartBody.Part.createFormData("avatar", avatar.getName(), requestFile)
        }

        val mobileBody = RequestBody.create(MediaType.parse("text/plain"), mobile)
        val codeBody = RequestBody.create(MediaType.parse("text/plain"), code)
        val firstNameBody = RequestBody.create(MediaType.parse("text/plain"), firstName)
        val lastNameBody = RequestBody.create(MediaType.parse("text/plain"), lastName)
        val birthDateBody = RequestBody.create(MediaType.parse("text/plain"), birthDate)
        val emailBody = RequestBody.create(MediaType.parse("text/plain"), email)
        val aboutMeBody = RequestBody.create(MediaType.parse("text/plain"), aboutMe)
        val aboutSpouseBody = RequestBody.create(MediaType.parse("text/plain"), aboutSpouse)

        return service.registerUser(mobileBody, codeBody, firstNameBody, lastNameBody, sex, birthDateBody, cityId, emailBody, aboutMeBody, aboutSpouseBody, avatarPart)
    }

    /**
     * Update user device info & FCN info for chart and send notification in server
     *
     * @param fcmId the firebase id of device
     * @param phoneId the imei or phone id
     * @param deviceType type of app (android/ios)
     */
    fun updateUserDetails(fcmId: String, phoneId: String, deviceType: String): Observable<BaseResponse<Any>> {
        return service.updateUserDetails(fcmId, phoneId, deviceType)
    }

    /**
     * login user to server
     *
     * @param mobile mobile number
     * @param code activision code that send by sms
     */
    fun loginUser(mobile: String, code: String): Observable<BaseResponse<UserRegisterResponse>> {
        return service.loginUser(mobile, code)
    }

    /**
     * edit user request
     *
     * @param editProfileRequest request of edit user
     *
     * @return BaseResponse<UserRegisterResponse>
     */
    fun editUser(editProfileRequest: EditProfileRequest): Observable<BaseResponse<UserRegisterResponse>> {
        return service.editUser(editProfileRequest.firstName, editProfileRequest.lastName, editProfileRequest.cityId, editProfileRequest.email, editProfileRequest.birthDate, editProfileRequest.aboutMe, editProfileRequest.aboutSpouse, editProfileRequest.sex)
    }

    /**
     * get list of province
     *
     * @return BaseResponse<ProvinceCityResponse>
     */
    fun getProvince(): Observable<BaseResponse<ProvinceCityResponse>> {
        return service.getProvinceList()
    }

    /**
     * get list of city from one province
     *
     * @param provinceId id of province
     *
     * @return BaseResponse<ProvinceCityResponse>>
     */
    fun getCity(provinceId: Int): Observable<BaseResponse<ProvinceCityResponse>> {
        return service.getCityList(provinceId = provinceId)
    }

    /**
     * upload an image for gallery
     *
     * @param image file of image from phone
     * @return BaseResponse check isSuccess
     */
    fun uploadImage(image: File): Observable<BaseResponse<Any>> {
        var imagePart: MultipartBody.Part? = null
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image)
        imagePart = MultipartBody.Part.createFormData("image", image.getName(), requestFile)
        return service.uploadImage(imagePart);
    }

    /**
     * upload avatar to server
     *
     * @param image file of image from phone
     * @return BaseResponse check isSuccess
     */
    fun uploadAvatar(image: File): Observable<BaseResponse<Any>> {
        var imagePart: MultipartBody.Part? = null
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image)
        imagePart = MultipartBody.Part.createFormData("avatar", image.getName(), requestFile)
        return service.uploadAvatar(imagePart);
    }

    /**
     * get My Meta data
     *
     * @return BaseResponse<MetaDataResponse>
     */
    fun getMetaData(): Observable<BaseResponse<MetaDataResponse>> {
        return service.getMetaData()
    }

    /**
     * get my Opponent meta data
     *
     * @return BaseResponse<MetaDataResponse>
     */
    fun getMetaDataOpponent(): Observable<BaseResponse<MetaDataResponse>> {
        return service.getMetaDataOpponent()
    }

    /**
     * Start new chat service
     *
     * @param userId witch user you want start chat
     * @return BaseResponse<StartChatResponse>
     */
    fun startChat(userId: Long): Observable<BaseResponse<StartChatResponse>> {
        return service.startChat(userId = userId)
    }

    /**
     * get chat details
     *
     * @param chatId chat server id
     * @param page page for pagination list view
     *
     * @return BaseResponse<ChatGetResponse>
     */
    fun getChat(chatId: Long, page: Int): Observable<BaseResponse<ChatGetResponse>> {
        return service.getChat(chatId, page)
    }

    /**
     * send new message
     * only one of message or image can be filled
     * @param chatId conversion id
     * @param message test message
     * @param image image file
     *
     * @return BaseResponse<StartChatResponse>
     */
    fun sendChat(chatId: Long, message: String?, image: File?): Observable<BaseResponse<StartChatResponse>> {
        var imagePart: MultipartBody.Part? = null
        if (image != null) {
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image)
            imagePart = MultipartBody.Part.createFormData("image", image.getName(), requestFile)
        }

        var messageBody: RequestBody? = null
        if (message != null) {
            messageBody = RequestBody.create(MediaType.parse("text/plain"), message)
        }
        return service.sendChat(chatId, messageBody, imagePart)
    }

    /**
     * Get all chat history
     *
     * @param page page for pagination list view
     *
     * @return BaseResponse<ChatHistoryResponse>
     */
    fun chatHistory(page: Int): Observable<BaseResponse<ChatHistoryResponse>> {
        return service.chatHistory(page)
    }

    /**
     * Add user to my favorites
     *
     * @param userId id of user
     *
     * @return Base Response (check status)
     */
    fun userFav(userId: Long): Observable<BaseResponse<Any>> {
        return service.userFav(userId)
    }

    /**
     * Delete user from my favorites
     *
     * @param favId favorite id
     *
     * @return BaseResponse<Any>
     */
    fun deleteUserFav(favId: Long): Observable<BaseResponse<Any>> {
        return service.deleteUserFav(favId)
    }

    /**
     * get pricing list
     *
     * @return BaseResponse<PaymentListResponse>
     */
    fun getPaymentList(): Observable<BaseResponse<PaymentListResponse>> {
        return service.getPaymentList()
    }

    /**
     * Delete account and all history
     *
     * @return BaseResponse<Any>
     */
    fun deleteAccount(): Observable<BaseResponse<Any>> {
        return service.deleteAccount()
    }

    /**
     * start new payment
     *
     * @param paymentId id of pricing list
     *
     * @return BaseResponse<GetPaymentResponse>>
     */
    fun getPayment(paymentId: Int): Observable<BaseResponse<GetPaymentResponse>> {
        return service.getPayment(paymentId)
    }

    /**
     * get list of my favorites
     *
     * @param page page for pagination list view
     *
     * @return BaseResponse<MyFavUser>
     */
    fun getMyUserFav(page: Int): Observable<BaseResponse<MyFavUser>> {
        return service.getMyUserFav(page)
    }

    /**
     * get List of user Favorites me
     *
     * @param page page for pagination list view
     *
     * @return BaseResponse<MyFavUser>
     */
    fun getUserFavMe(page: Int): Observable<BaseResponse<MyFavUser>> {
        return service.getUserFavMe(page)
    }

    /**
     * get my gallery list
     *
     * @return BaseResponse<ImageResponse>>
     */
    fun getMyGallery(): Observable<BaseResponse<ImageResponse>> {
        return service.getMyGalley()
    }

    /**
     * delete image from gallery
     *
     * @param id id of image
     *
     * @return base response check isSuccess
     */
    fun deleteGallery(id: Long): Observable<BaseResponse<Any>> {
        return service.deleteGallery(id)
    }

    /**
     * get older tickets
     *
     * @param page page for pagination list view
     *
     * @return BaseResponse<ContactUsResponse>
     */
    fun getConnectUs(page: Int): Observable<BaseResponse<ContactUsResponse>> {
        return service.getConnectUs(page)
    }

    /**
     * send new ticket
     *
     * @param title title of ticket
     * @param message message of ticket (description)
     * @param type type of ticket (read server document for ticket
     *
     * @return BaseResponse<ContactUsResponse>
     */
    fun sendConnectUs(title: String, message: String, type: String): Observable<BaseResponse<ContactUsResponse>> {
        return service.sendConnectUs(title, message, type)
    }

    /**
     * get app settings list from server
     *
     * @return BaseResponse<SettingResponse>
     */
    fun getSettings(): Observable<BaseResponse<SettingResponse>> {
        return service.getSettings()
    }

    /**
     * get latest version of app has been released
     *
     * @return BaseResponse<AppUpdateResponse>
     */
    fun checkAppUpdate(): Observable<BaseResponse<AppUpdateResponse>> {
        return service.checkAppUpdate()
    }

    /**
     * get advertising banner images
     *
     * @return BaseResponse<BannerResponse>
     */
    fun getBanners(): Observable<BaseResponse<BannerResponse>> {
        return service.getBannerAds()
    }

    /**
     * search user in one city
     *
     * @param page page for pagination list view
     * @param cityId id of city search on it
     *
     * @return BaseResponse<UserDetailsResponse>
     */
    fun search(page: Int, cityId: Int): Observable<BaseResponse<UserDetailsResponse>> {
        return service.search(page, cityId)
    }

    /**
     * block user chat
     *
     * @param chatId id of chat
     *
     * @return response check isSuccess
     */
    fun blockChat(chatId: Long): Observable<BaseResponse<Any>> {
        return service.blockChat(chatId)
    }

    /**
     * unblock Chat
     *
     * @param chatId id of chat
     *
     * @return response check isSuccess
     */
    fun unBlockChat(chatId: Long): Observable<BaseResponse<Any>> {
        return service.unBlockChat(chatId)
    }

}