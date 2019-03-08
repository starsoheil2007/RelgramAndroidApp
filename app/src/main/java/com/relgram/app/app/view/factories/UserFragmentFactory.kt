package com.relgram.app.app.view.factories

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.app.Fragment
import com.relgram.app.app.view_models.GalleryFragmentViewModel
import com.relgram.app.app.view_models.UserFragmentViewModel


@Suppress("UNCHECKED_CAST")
class UserFragmentFactory(private val fragment: Fragment) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserFragmentViewModel::class.java)) {
            return UserFragmentViewModel() as T
        }

        if (modelClass.isAssignableFrom(GalleryFragmentViewModel::class.java)) {
            return GalleryFragmentViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}