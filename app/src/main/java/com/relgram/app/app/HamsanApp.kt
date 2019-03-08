package com.relgram.app.app

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.multidex.MultiDexApplication
import com.google.firebase.iid.FirebaseInstanceId
import com.relgram.app.app.database.AppDatabases
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HamsanApp : MultiDexApplication() {

    companion object {
        fun checkForUserDetailsUpdate() {
            if (AppDatabases.dbInstance!!.userInfoDao().getToken().isNullOrEmpty()) {
                return
            }

            val id = getAndroidId()

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener { instanceIdResult ->
                val callRest = WebService().updateUserDetails(instanceIdResult.token, id, "android").subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe {
                        }
                        .doOnTerminate {
                        }
                        .subscribe(
                                { result ->

                                },
                                { error -> print(error.message) }
                        )
            }


        }

        fun getAndroidId(): String {
            return "35" + //we make this look like a valid IMEI

                    Build.BOARD.length % 10 + Build.BRAND.length % 10 +
                    Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 +
                    Build.DISPLAY.length % 10 + Build.HOST.length % 10 +
                    Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +
                    Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +
                    Build.TAGS.length % 10 + Build.TYPE.length % 10 +
                    Build.USER.length % 10
        }


        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }


}