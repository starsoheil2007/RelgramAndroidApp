package com.relgram.app.app.view.factories

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.app.Fragment
import com.relgram.app.app.view_models.RegisterFragmentViewModel
import com.relgram.app.app.view_models.RolesAcceptFragmentViewModel

@Suppress("UNCHECKED_CAST")
class RegisterFragmentFactory(private val fragment: Fragment) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterFragmentViewModel::class.java)) {
            return RegisterFragmentViewModel() as T
        }

        if (modelClass.isAssignableFrom(RolesAcceptFragmentViewModel::class.java)) {
            return RolesAcceptFragmentViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}