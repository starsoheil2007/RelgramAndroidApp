package com.relgram.app.app.view.activities

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ActivityWebViewBinding
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view_models.WebViewActivityViewModel

/**
 * This Activity is for how roles & other html tag get from server
 *
 */
class WebViewActivity : AppCompatActivity() {


    private lateinit var binding: ActivityWebViewBinding
    private lateinit var viewModel: WebViewActivityViewModel

    var html: String? = null

    companion object {
        const val HTML_DATA = "html"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this)).get(WebViewActivityViewModel::class.java)
        initToolBar()
        binding.viewModel = viewModel

        if (intent.extras.getString(WebViewActivity.HTML_DATA, null) != null) {
            html = intent.extras.getString(WebViewActivity.HTML_DATA)
        }

        val style = "<style type =\"text/css\">\n" + "        @font-face {\n" + "            font-family: MyFont;\n" + "            src: url(\"file:///android_asset/fonts/iransans.ttf\")\n" + "        }\n" + "        body {\n" + "            font-family: MyFont;\n" + "            font-size: medium;\n" + "            text-align: justify;\n" + "        }</style>"
        html = "<!DOCTYPE html><html><head>$style</head><body dir=\"rtl\">$html</body><html>"

        binding.webView.settings.allowFileAccess = true
        binding.webView.settings.domStorageEnabled = true;

        binding.webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null)
    }

    private fun initToolBar() {
        setSupportActionBar(binding.include.mainToolbar);
        if (getSupportActionBar() != null) getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
        binding.include.toolbarTitle.text = resources.getString(R.string.relgram)
        binding.include.iconMessage.visibility = View.GONE
        binding.include.iconUser.visibility = View.GONE
    }
}
