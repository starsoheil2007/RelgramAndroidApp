package com.relgram.app.app.view_models

import android.view.View
import android.widget.AdapterView
import com.relgram.app.view_models.BaseViewModel

/**
 * Register fragment view model to handle spinners
 *
 */
class RegisterFragmentViewModel : BaseViewModel() {


    public lateinit var onProvinceSelectedListener: onProvinceSelected
    public lateinit var onCitySelectedListener: onCitySelected

    /**
     * handle on select a province in spinner
     *
     * @param parent
     * @param view
     * @param pos
     * @param id id of province
     */
    fun onSelectItem(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        onProvinceSelectedListener.onSelected(id)
    }

    /**
     * handle on select a province in spinner
     *
     * @param parent
     * @param view
     * @param pos
     * @param id id of city
     */
    fun onCitySelectItem(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        onCitySelectedListener.onSelected(id)
    }


    interface onProvinceSelected {
        fun onSelected(id: Long)
    }


    interface onCitySelected {
        fun onSelected(id: Long)
    }


}