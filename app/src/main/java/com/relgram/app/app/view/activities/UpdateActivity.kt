package com.relgram.app.app.view.activities

import android.Manifest
import android.app.DownloadManager
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.relgram.app.app.BuildConfig
import com.relgram.app.app.HamsanApp
import com.relgram.app.app.HamsanApp.Companion.context
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ActivityUpdateBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.models.AppUpdateResponse
import com.relgram.app.app.view.dialog.LoadingDialog
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view_models.UpdateActivityViewModel
import java.io.File

/**
 * Update Activity for show latest version is exist
 *
 */
class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var viewModel: UpdateActivityViewModel

    companion object {
        const val VERSION_CODE = "code"
        const val VERSION_NAME = "name"
        const val FORCE_UPDATE = "force"
        const val CHANGE = "change"
        const val DOWNLOAD_LINK = "link"
        const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 108
    }

    public var loading: LoadingDialog? = null


    var versionCode: Int? = null
    var versionName: String? = null
    var forceUpdate: Boolean? = null
    var change: String? = null
    var downloadLink: String? = null
    var oldVersionName: String? = null

    public var updateResponse: AppUpdateResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this)).get(UpdateActivityViewModel::class.java)
        initToolBar()
        if (intent.extras != null) {
            versionCode = intent.extras.getInt(VERSION_CODE)
            versionName = intent.extras.getString(VERSION_NAME)
            forceUpdate = intent.extras.getBoolean(FORCE_UPDATE)
            change = intent.extras.getString(CHANGE)
            downloadLink = intent.extras.getString(DOWNLOAD_LINK)
        }

        try {
            val pInfo = HamsanApp.context.getPackageManager().getPackageInfo(packageName, 0)
            oldVersionName = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
        }
        viewModel.bind(versionCode, versionName, oldVersionName, forceUpdate, change, downloadLink, this)
        binding.viewModel = viewModel

        binding.downloadApp.typeface = FontHelper.instance.getPersianTextTypeface()
        binding.downloadApp.setOnClickListener {
            checkPermossionDownload()
        }

        loading = LoadingDialog.newInstance(resources.getString(R.string.loading))

    }

    private fun initToolBar() {
        setSupportActionBar(binding.include.mainToolbar)
        if (getSupportActionBar() != null)
            getSupportActionBar()!!.setDisplayShowTitleEnabled(false)
        binding.include.toolbarTitle.text = resources.getText(R.string.newVersionTitle)
        binding.include.iconMessage.visibility = View.GONE
        binding.include.iconUser.visibility = View.GONE
    }

    fun downloadAppInstall() {
        if (!loading!!.isAdded)
            loading!!.show(supportFragmentManager, "dialog")
        var destination: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val fileName = "RelGramNew.apk"
        destination = """$destination/$fileName"""
        val uri = Uri.parse("file://$destination")

        //Delete update file if exists
        val file = File(destination)
        if (file.exists())
        //file.delete() - test this, I think sometimes it doesnt work
            file.delete()

        //get url of app on server

        //set downloadmanager
        val request = DownloadManager.Request(Uri.parse(downloadLink))
        request.setDescription(resources.getString(R.string.downloadApp))
        request.setTitle(resources.getString(R.string.app_name))

        //set destination
        request.setDestinationUri(uri)

        // get download service and enqueue file
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = manager.enqueue(request)

        //set BroadcastReceiver to install app when .apk is downloaded
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context, intent: Intent) {
                loading!!.dismiss()

                var toInstall = File(destination);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", toInstall);
                    val intent = Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setData(apkUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent)
                } else {
                    val apkUri = Uri.fromFile(toInstall);
                    val intent = Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                unregisterReceiver(this)
                finish()
            }
        }
        //register receiver for when .apk download is compete
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun checkPermossionDownload() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            downloadAppInstall()
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES),
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // pick image after request permission success
                    downloadAppInstall()
                }
            }
        }
    }


}
