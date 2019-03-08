package com.relgram.app.app.view.factories

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.app.Fragment
import com.relgram.app.app.view_models.EditMetaDataFragmentViewModel
import com.relgram.app.app.view_models.EditSpouseMetaFragmentViewModel
import com.relgram.app.app.view_models.FavoriteFragmentViewModel

@Suppress("UNCHECKED_CAST")
class FavoriteFragmentFactory(private val fragment: Fragment) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteFragmentViewModel::class.java)) {
            return FavoriteFragmentViewModel() as T
        }

        if (modelClass.isAssignableFrom(EditMetaDataFragmentViewModel::class.java)) {
            return EditMetaDataFragmentViewModel() as T
        }

        if (modelClass.isAssignableFrom(EditSpouseMetaFragmentViewModel::class.java)) {
            return EditSpouseMetaFragmentViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}