package com.relgram.app.app.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.relgram.app.app.HamsanApp.Companion.context
import com.relgram.app.app.R
import com.relgram.app.app.library.PersianTextView
import com.relgram.app.app.models.ProvinceCityResponse

class ProvinceCitySpinnerAdapter : BaseAdapter {

    lateinit var items: List<ProvinceCityResponse>

    constructor(items: List<ProvinceCityResponse>) : super() {
        this.items = items
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val myView = LayoutInflater.from(context).inflate(R.layout.spinner_row, parent, false)
        val title = myView.findViewById(R.id.textTitle) as PersianTextView
        title.setTextColor(context.resources.getColor(R.color.white))
        title.text = items.get(position).name
        return myView

    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val myView = LayoutInflater.from(context).inflate(R.layout.spinner_dropdown_row, parent, false)
        val title = myView.findViewById(R.id.textTitle) as PersianTextView
        title.text = items.get(position).name
        return myView
    }

    override fun getItem(position: Int): ProvinceCityResponse {
        return items.get(position)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }
}