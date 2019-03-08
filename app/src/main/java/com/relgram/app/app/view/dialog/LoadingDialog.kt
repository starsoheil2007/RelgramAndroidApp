package com.relgram.app.app.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import com.relgram.app.app.R
import com.relgram.app.app.databinding.DialogLoadingBinding
import com.relgram.app.app.view_models.DialogLoadingViewModel

/**
 * Loading Dialog Creator
 *
 */
class LoadingDialog : DialogFragment() {

    private lateinit var binding: DialogLoadingBinding
    public lateinit var viewModel: DialogLoadingViewModel

    companion object {
        const val KEY_TEXT = "text"
        /**
         * Create a new instance of Loading Dialog
         *
         * @param loadingText the text of loading dialog
         */
        fun newInstance(loadingText: String): LoadingDialog {
            val dialog = LoadingDialog()
            dialog.arguments = Bundle()
            dialog.getArguments()!!.putString(KEY_TEXT, loadingText)
            dialog.isCancelable = false
            return dialog
        }

    }


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = DialogLoadingViewModel()
        viewModel.bind(arguments!!.getString(KEY_TEXT))
    }

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_loading, null, false)
        binding.viewModel = viewModel

        val alertDialog = AlertDialog.Builder(activity, R.style.Base_Theme_MaterialComponents_Dialog)
                .setView(binding.root)
                .create()
        alertDialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        return alertDialog
    }
}