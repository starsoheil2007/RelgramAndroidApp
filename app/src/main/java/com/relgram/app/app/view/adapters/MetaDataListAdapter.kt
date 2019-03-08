package com.relgram.app.app.view.adapters

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import com.google.gson.Gson
import com.relgram.app.app.HamsanApp
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ListRowMetaInputBinding
import com.relgram.app.app.databinding.ListRowMetaMultiSelectBinding
import com.relgram.app.app.databinding.ListRowMetaOneSelectBinding
import com.relgram.app.app.databinding.ListRowMetaTrueSelectBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.models.MetaDataResponse
import com.relgram.app.app.view_models.ListMetaDataViewModel


class MetaDataListAdapter(var context: Context, var isOtherUserId: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var metaList = ArrayList<MetaDataResponse>()

    private var selectedItems: HashMap<Int, String> = HashMap()
    lateinit var bindingMultiSelect: ListRowMetaMultiSelectBinding
    lateinit var bindingOneSelect: ListRowMetaOneSelectBinding
    lateinit var bindingTrueFalse: ListRowMetaTrueSelectBinding
    lateinit var bindingInput: ListRowMetaInputBinding

    companion object {
        const val TYPE_MULTI_SELECT = 1
        const val TYPE_OME_SELECT = 2
        const val TYPE_TRUE_FALSE = 3
        const val TYPE_INPUT = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_MULTI_SELECT -> {
                bindingMultiSelect = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_row_meta_multi_select, parent, false)
                return MultiViewHolder(bindingMultiSelect, selectedItems, isOtherUserId)
            }
            TYPE_OME_SELECT -> {
                bindingOneSelect = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_row_meta_one_select, parent, false)
                return OneViewHolder(bindingOneSelect, selectedItems, isOtherUserId)

            }
            TYPE_TRUE_FALSE -> {
                bindingTrueFalse = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_row_meta_true_select, parent, false)
                return TrueViewHolder(bindingTrueFalse, selectedItems, isOtherUserId)
            }
            else -> {
                bindingInput = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_row_meta_input, parent, false)
                return InputViewHolder(bindingInput, selectedItems, isOtherUserId)
            }
        }
    }

    override fun getItemCount(): Int {
        return metaList.size
    }

    override fun getItemViewType(position: Int): Int {
        return metaList[position].typeField.toInt()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_MULTI_SELECT -> {
                var multiViewHolder = holder as MultiViewHolder
                multiViewHolder.selectedItems = selectedItems
                return multiViewHolder.bind(metaList[position])
            }
            TYPE_OME_SELECT -> {
                var oneViewHolder = holder as OneViewHolder
                oneViewHolder.selectedItems = selectedItems
                return oneViewHolder.bind(metaList[position])
            }
            TYPE_TRUE_FALSE -> {
                var trueViewHolder = holder as TrueViewHolder
                trueViewHolder.selectedItems = selectedItems
                return trueViewHolder.bind(metaList[position])
            }

            TYPE_INPUT -> {
                var inputViewHolder = holder as InputViewHolder
                inputViewHolder.selectedItems = selectedItems
                return inputViewHolder.bind(metaList[position])
            }
        }
    }

    fun updatePostList(metaList: List<MetaDataResponse>) {
        this.metaList.addAll(metaList)
        notifyDataSetChanged()
    }

    fun updateSelectedList(selectedItems: HashMap<Int, String>) {
        this.selectedItems = selectedItems
        notifyDataSetChanged()
    }

    fun getSelectedMeta(): String {
        val gson = Gson()
        return gson.toJson(selectedItems)
    }

    class MultiViewHolder(private var bindingMultiSelect: ListRowMetaMultiSelectBinding, var selectedItems: HashMap<Int, String>, var isOtherUserId: Boolean) : RecyclerView.ViewHolder(bindingMultiSelect.root) {
        private val viewModel = ListMetaDataViewModel()
        fun bind(meta: MetaDataResponse) {
            viewModel.bind(meta)
            bindingMultiSelect.checkBox.removeAllViews()
            for (i in meta.values) {
                val cb = CheckBox(HamsanApp.context)
                cb.text = i.title
                cb.tag = i.id
                cb.isEnabled = !isOtherUserId
                cb.typeface = FontHelper.instance.getPersianTextTypeface()
                if (selectedItems.containsKey(i.id)) {
                    cb.isChecked = selectedItems.get(i.id).equals("true")
                }
                cb.setOnCheckedChangeListener { compoundButton, b ->
                    manageSelected(compoundButton.tag as Int, b.toString())
                }
                bindingMultiSelect.checkBox.addView(cb)
            }
            bindingMultiSelect.viewModel = viewModel
        }

        fun manageSelected(key: Int, value: String) {
            if (selectedItems.containsKey(key)) {
                selectedItems[key] = value
            } else {
                selectedItems.put(key, value)
            }

        }

    }


    class OneViewHolder(var bindingOneSelect: ListRowMetaOneSelectBinding, var selectedItems: HashMap<Int, String>, var isOtherUserId: Boolean) : RecyclerView.ViewHolder(bindingOneSelect.root) {
        private val viewModel = ListMetaDataViewModel()
        fun bind(meta: MetaDataResponse) {
            viewModel.bind(meta)
            bindingOneSelect.radioBox.removeAllViews()
            for (i in meta.values) {
                val cb = RadioButton(HamsanApp.context)
                cb.text = i.title
                cb.tag = i.id
                cb.isEnabled = !isOtherUserId
                cb.typeface = FontHelper.instance.getPersianTextTypeface()
                if (selectedItems.containsKey(i.id)) {
                    cb.isChecked = selectedItems.get(i.id).equals("true")
                }
                cb.setOnCheckedChangeListener { compoundButton, b ->
                    manageSelected(compoundButton.tag as Int, b.toString())
                }
                bindingOneSelect.radioBox.addView(cb)
            }
            bindingOneSelect.viewModel = viewModel

        }

        fun manageSelected(key: Int, value: String) {
            if (selectedItems.containsKey(key)) {
                selectedItems[key] = value
            } else {
                selectedItems.put(key, value)
            }
        }

    }

    class TrueViewHolder(var bindingTrueFalse: ListRowMetaTrueSelectBinding, var selectedItems: HashMap<Int, String>, var isOtherUserId: Boolean) : RecyclerView.ViewHolder(bindingTrueFalse.root) {
        private val viewModel = ListMetaDataViewModel()
        fun bind(meta: MetaDataResponse) {
            viewModel.bind(meta)
            bindingTrueFalse.selected.tag = meta.values[0].id
            bindingTrueFalse.selected.typeface = FontHelper.instance.getPersianTextTypeface()
            bindingTrueFalse.selected.isEnabled = !isOtherUserId
            if (selectedItems.containsKey(meta.values[0].id)) {
                bindingTrueFalse.selected.isChecked = selectedItems.get(meta.values[0].id).equals("true")
            }
            bindingTrueFalse.selected.setOnCheckedChangeListener { compoundButton, b ->
                manageSelected(compoundButton.tag as Int, b.toString())
            }
            bindingTrueFalse.viewModel = viewModel
        }

        fun manageSelected(key: Int, value: String) {
            if (selectedItems.containsKey(key)) {
                selectedItems[key] = value
            } else {
                selectedItems.put(key, value)
            }

        }
    }


    class InputViewHolder(var bindingInput: ListRowMetaInputBinding, var selectedItems: HashMap<Int, String>, var isOtherUserId: Boolean) : RecyclerView.ViewHolder(bindingInput.root) {
        private val viewModel = ListMetaDataViewModel()
        fun bind(meta: MetaDataResponse) {
            viewModel.bind(meta)
            bindingInput.editText.tag = meta.values[0].id
            bindingInput.editText.isEnabled = !isOtherUserId
            if (selectedItems.containsKey(meta.values[0].id)) {
                bindingInput.editText.setText(selectedItems.get(meta.values[0].id))
            }
            bindingInput.editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    manageSelected(bindingInput.editText.tag as Int, bindingInput.editText.text.toString())
                }
            })
            bindingInput.viewModel = viewModel
        }

        fun manageSelected(key: Int, value: String) {
            if (selectedItems.containsKey(key)) {
                selectedItems[key] = value
            } else {
                selectedItems.put(key, value)
            }
        }
    }

}