package com.relgram.app.app.view.factories

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.app.Fragment
import com.relgram.app.app.view_models.SearchFragmentViewModel

@Suppress("UNCHECKED_CAST")
class SearchFragmentFactory(private val fragment: Fragment) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchFragmentViewModel::class.java)) {
            return SearchFragmentViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}