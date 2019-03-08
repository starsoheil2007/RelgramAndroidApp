package com.relgram.app.app.view.activities

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.relgram.app.app.R
import com.relgram.app.app.database.AppDatabases
import com.relgram.app.app.databinding.ActivityNotificationBinding
import com.relgram.app.app.view.adapters.NotificationListAdapter
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view_models.NotificationActivityViewModel

/**
 * show notification
 *
 */
class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private lateinit var viewModel: NotificationActivityViewModel
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: NotificationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this)).get(NotificationActivityViewModel::class.java)
        binding.viewModel = viewModel
        initToolBar()
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.mainList.layoutManager = layoutManager
        var allNote = AppDatabases.dbInstance!!.notificationDao().getAll()
        if (allNote != null) {
            adapter = NotificationListAdapter()
            adapter.updatePostList(allNote)
            binding.mainList.adapter = adapter
        }
    }

    private fun initToolBar() {
        setSupportActionBar(binding.include.mainToolbar)
        if (getSupportActionBar() != null) getSupportActionBar()!!.setDisplayShowTitleEnabled(false)
        binding.include.toolbarTitle.text = resources.getText(R.string.notifications)
        binding.include.iconMessage.visibility = View.GONE
        binding.include.iconUser.visibility = View.GONE
    }
}
