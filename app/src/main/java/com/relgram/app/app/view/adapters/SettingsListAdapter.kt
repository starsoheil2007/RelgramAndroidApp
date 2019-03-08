package com.relgram.app.app.view.adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.relgram.app.app.R
import com.relgram.app.app.database.AppDatabases
import com.relgram.app.app.databinding.ListRowSettingsBinding
import com.relgram.app.app.view.activities.ContactActivity
import com.relgram.app.app.view.activities.EditUserProfileActivity
import com.relgram.app.app.view.activities.InfoActivity
import com.relgram.app.app.view.activities.WebViewActivity
import com.relgram.app.app.view.dialog.LoadingDialog
import com.relgram.app.app.view_models.SettingsListViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SettingsListAdapter(var context: Context, var onFinish: onFinishAppRequested) : RecyclerView.Adapter<SettingsListAdapter.ViewHolder>() {

    private var settingsList = ArrayList<SettingsListViewModel>()
    lateinit var binding: ListRowSettingsBinding
    public var loading: LoadingDialog = LoadingDialog.newInstance(context.resources.getString(R.string.loading))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_row_settings, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(settingsList[position])
        binding.root.setOnClickListener {
            when (settingsList[position].id) {
                1 -> {
                    val intent = Intent(context, EditUserProfileActivity::class.java)
                    context.startActivity(intent)
                }
                2 -> {
                    exitAccountDialog()
                }
                3 -> {
                    deleteAccountDialog()
                }
                4 -> {
                    var settings = AppDatabases.dbInstance!!.settingsDao().getById("1")
                    if (settings != null) {
                        val intent = Intent(this.context, WebViewActivity::class.java)
                        intent.putExtra(WebViewActivity.HTML_DATA, settings.value)
                        context.startActivity(intent)
                    }
                }
                5 -> {
                    var settings = AppDatabases.dbInstance!!.settingsDao().getById("4")
                    if (settings != null) {
                        val intent = Intent(this.context, WebViewActivity::class.java)
                        intent.putExtra(WebViewActivity.HTML_DATA, settings.value)
                        context.startActivity(intent)
                    }
                }
                6 -> {
                    val intent = Intent(context, ContactActivity::class.java)
                    context.startActivity(intent)
                }
                7 -> {
                    val intent = Intent(context, InfoActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return settingsList.size
    }

    fun updatePostList(paymentList: List<SettingsListViewModel>) {
        this.settingsList.addAll(paymentList)
        notifyDataSetChanged()
    }


    class ViewHolder(private val binding: ListRowSettingsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(set: SettingsListViewModel) {
            val viewModel = SettingsListViewModel(set.id, set.title, set.icon, set.iconColor)
            binding.viewModel = viewModel
        }
    }


    fun deleteAccountDialog() {
        val alertDialog = AlertDialog.Builder(this.context).create()
        alertDialog.setTitle(context.resources.getString(R.string.deleteAccountTitle))
        alertDialog.setMessage(context.resources.getText(R.string.deleteAccountMessage))
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.resources.getText(R.string.dialogNo)) { dialogInterface: DialogInterface, i: Int ->
            alertDialog.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.resources.getText(R.string.dialogYes)) { dialog, which ->
            deleteAccount()
        }
        alertDialog.show()
    }

    fun exitAccountDialog() {
        val alertDialog = AlertDialog.Builder(this.context!!).create()
        alertDialog.setTitle(context.resources.getString(R.string.exitAccountTitle))
        alertDialog.setMessage(context.resources.getText(R.string.exitAccountMessage))
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.resources.getText(R.string.dialogNo)) { dialogInterface: DialogInterface, i: Int ->
            alertDialog.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.resources.getText(R.string.dialogYes)) { dialog, which ->
            exitAccount()
        }
        alertDialog.show()
    }

    private fun exitAccount() {
        AppDatabases.dbInstance!!.userInfoDao().deleteAll()
        onFinish.Onfinish()
    }


    private fun deleteAccount() {
        val callRest = WebService().deleteAccount().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (!loading!!.isAdded)
                        loading!!.show((context as AppCompatActivity).supportFragmentManager, "dialog")
                }
                .doOnTerminate {
                    loading!!.dismiss()
                }
                .subscribe(
                        { result ->
                            if (result.isSuccess) {
                                AppDatabases.dbInstance!!.userInfoDao().deleteAll()
                                onFinish.Onfinish()
                            }
                        },
                        { error -> print(error.message) }
                )

    }

    public interface onFinishAppRequested {
        fun Onfinish()

    }

}