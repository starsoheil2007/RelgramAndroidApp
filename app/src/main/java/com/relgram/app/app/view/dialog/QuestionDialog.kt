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
 * Question Dialog Creator
 * Use in next version
 *
 */
class QuestionDialog : DialogFragment() {
    private lateinit var binding: DialogLoadingBinding
    public lateinit var viewModel: DialogLoadingViewModel

    companion object {
        const val KEY_TEXT = "text"
    }

    fun newInstance(loadingText: String): LoadingDialog {
        val dialog = LoadingDialog()
        dialog.arguments!!.putString(KEY_TEXT, loadingText)
        return dialog
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

        return AlertDialog.Builder(activity, R.style.AlertDialog_AppCompat)
                .setView(binding.root)
                .create()
    }
}