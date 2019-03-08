package com.relgram.app.app.view.activities

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ActivitySettingsBinding
import com.relgram.app.app.view.adapters.SettingsListAdapter
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view_models.SettingsActivityViewModel
import com.relgram.app.app.view_models.SettingsListViewModel

/**
 * Show Settings of application
 *
 * in oncreate function we have static array to fill settings RecyclerView
 *
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsActivityViewModel
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: SettingsListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this)).get(SettingsActivityViewModel::class.java)
        binding.viewModel = viewModel
        initToolBar()
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.mainList.layoutManager = layoutManager

        val settingsList = ArrayList<SettingsListViewModel>()
        settingsList.add(SettingsListViewModel(1, resources.getString(R.string.editProfile), resources.getString(R.string.iconPersonPin), resources.getColor(R.color.colorPrimaryDark)))
        settingsList.add(SettingsListViewModel(2, resources.getString(R.string.logoutAccount), resources.getString(R.string.iconExit), resources.getColor(R.color.logoutStartColor)))
        settingsList.add(SettingsListViewModel(3, resources.getString(R.string.deleteAcount), resources.getString(R.string.iconDelete), resources.getColor(R.color.colorCount)))
        settingsList.add(SettingsListViewModel(4, resources.getString(R.string.roles), resources.getString(R.string.iconMessageRead), resources.getColor(R.color.brown)))
        settingsList.add(SettingsListViewModel(5, resources.getString(R.string.ahkamEzdevaj), resources.getString(R.string.iconHokm), resources.getColor(R.color.green)))
        settingsList.add(SettingsListViewModel(6, resources.getString(R.string.contactUs), resources.getString(R.string.iconPhone), resources.getColor(R.color.prubble)))
        settingsList.add(SettingsListViewModel(7, resources.getString(R.string.about), resources.getString(R.string.iconWarning), resources.getColor(R.color.blueDark)))
        adapter = SettingsListAdapter(this, object : SettingsListAdapter.onFinishAppRequested {
            override fun Onfinish() {
                setResult(MainActivity.FINISH_APP_RESULT_CODE)
                finish()
            }
        })
        adapter.updatePostList(settingsList)
        binding.mainList.adapter = adapter
    }

    /**
     * create Custom toolbar and action button
     *
     */
    private fun initToolBar() {
        setSupportActionBar(binding.include.mainToolbar);
        if (getSupportActionBar() != null) getSupportActionBar()!!.setDisplayShowTitleEnabled(false)
        binding.include.toolbarTitle.text = resources.getText(R.string.settings)
        binding.include.iconMessage.visibility = View.GONE
        binding.include.iconUser.visibility = View.GONE
    }
}
