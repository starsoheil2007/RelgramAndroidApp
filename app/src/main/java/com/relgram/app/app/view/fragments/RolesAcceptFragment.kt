package com.relgram.app.app.view.fragments

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.relgram.app.app.R
import com.relgram.app.app.database.AppDatabases
import com.relgram.app.app.databinding.FragmentWebViewBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.view.activities.LoginRegisterActivity
import com.relgram.app.app.view.factories.RegisterFragmentFactory
import com.relgram.app.app.view_models.RolesAcceptFragmentViewModel

class RolesAcceptFragment : Fragment() {


    private lateinit var binding: FragmentWebViewBinding
    private lateinit var viewModel: RolesAcceptFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_web_view, container, false)
        viewModel = ViewModelProviders.of(this, RegisterFragmentFactory(this)).get(RolesAcceptFragmentViewModel::class.java)
        binding.viewModel = viewModel


        val roleSettings = AppDatabases.dbInstance!!.settingsDao().getById("1")
        if (roleSettings != null) {
            var html = roleSettings.value
            val style = "<style type =\"text/css\">\n" +
                    "        @font-face {\n" +
                    "            font-family: MyFont;\n" +
                    "            src: url(\"file:///android_asset/fonts/iransans.ttf\")\n" +
                    "        }\n" +
                    "        body {\n" +
                    "            font-family: MyFont;\n" +
                    "            font-size: medium;\n" +
                    "            text-align: justify;\n" +
                    "        }</style>"
            html = "<!DOCTYPE html><html><head>$style</head><body dir=\"rtl\">$html</body><html>"

            binding.webView.settings.allowFileAccess = true
            binding.webView.settings.domStorageEnabled = true;

            binding.webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null)
        }
        binding.acceptRoles.typeface = FontHelper.instance.getPersianTextTypeface()
        binding.acceptRoles.setOnClickListener {
            (activity as LoginRegisterActivity).showRegisterFragment()
        }

        return binding.root
    }
}