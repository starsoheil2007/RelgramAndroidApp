package com.relgram.app.app.view.factories

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v7.app.AppCompatActivity
import com.relgram.app.app.view_models.*


@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel() as T
        }

        if (modelClass.isAssignableFrom(UpdateActivityViewModel::class.java)) {
            return UpdateActivityViewModel() as T
        }

        if (modelClass.isAssignableFrom(EditProfileActivityViewModel::class.java)) {
            return EditProfileActivityViewModel() as T
        }

        if (modelClass.isAssignableFrom(ChatDetailsActivityViewModel::class.java)) {
            return ChatDetailsActivityViewModel() as T
        }

        if (modelClass.isAssignableFrom(ContactActivityViewModel::class.java)) {
            return ContactActivityViewModel() as T
        }

        if (modelClass.isAssignableFrom(InfoActivityViewModel::class.java)) {
            return InfoActivityViewModel() as T
        }

        if (modelClass.isAssignableFrom(LoginRegisterActivityViewModel::class.java)) {
            return LoginRegisterActivityViewModel() as T
        }

        if (modelClass.isAssignableFrom(PaymentActivityViewModel::class.java)) {
            return PaymentActivityViewModel() as T
        }

        if (modelClass.isAssignableFrom(RolesActivityViewModel::class.java)) {
            return RolesActivityViewModel() as T
        }

        if (modelClass.isAssignableFrom(SendContactActivityViewModel::class.java)) {
            return SendContactActivityViewModel() as T
        }


        if (modelClass.isAssignableFrom(SplashActivityViewModel::class.java)) {
            return SplashActivityViewModel() as T
        }

        if (modelClass.isAssignableFrom(UserProfileViewActivityViewModel::class.java)) {
            return UserProfileViewActivityViewModel() as T
        }

        if (modelClass.isAssignableFrom(ChatMessagingActivityViewModel::class.java)) {
            return ChatMessagingActivityViewModel() as T
        }

        if (modelClass.isAssignableFrom(SettingsActivityViewModel::class.java)) {
            return SettingsActivityViewModel() as T
        }

        if (modelClass.isAssignableFrom(WebViewActivityViewModel::class.java)) {
            return WebViewActivityViewModel() as T
        }

        if (modelClass.isAssignableFrom(NotificationActivityViewModel::class.java)) {
            return NotificationActivityViewModel() as T
        }

        if (modelClass.isAssignableFrom(ChargeNeedActivityViewModel::class.java)) {
            return ChargeNeedActivityViewModel() as T
        }


        throw IllegalArgumentException("Unknown ViewModel class")
    }
}