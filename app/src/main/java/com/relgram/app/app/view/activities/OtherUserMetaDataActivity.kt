package com.relgram.app.app.view.activities

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ActivityOtherUserMetaDataBinding
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view.fragments.EditMyMetaDataFragment
import com.relgram.app.app.view.fragments.EditSpouseMetaDataFragment
import com.relgram.app.app.view_models.EditProfileActivityViewModel

/**
 * show users meta data in this activity
 *
 */
class OtherUserMetaDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtherUserMetaDataBinding
    private lateinit var viewModel: EditProfileActivityViewModel

    public var userId: Long? = null

    companion object {
        const val USER_ID = "userId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_user_meta_data)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this))
                .get(EditProfileActivityViewModel::class.java)

        initToolBar()

        if (intent.extras != null) {
            userId = intent.extras.getLong(USER_ID)
        }

        binding.segmentedView.setOnCheckedChangeListener { p0, p1 ->
            if (p1 == R.id.myMetaData) {
                var fragment = EditMyMetaDataFragment()
                fragment.otherUserId = this.userId!!
                showFragment(fragment)
            } else if (p1 == R.id.spouseMetaData) {
                var fragment = EditSpouseMetaDataFragment()
                fragment.otherUserId = this.userId!!
                showFragment(fragment)
            }
        }

    }

    /**
     * init custom toolbar and custom actions
     *
     */
    private fun initToolBar() {
        setSupportActionBar(binding.include.mainToolbar);
        if (getSupportActionBar() != null) getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
        binding.include.iconMessage.visibility = View.VISIBLE
        binding.include.iconMessage.visibility = View.GONE
    }

    /**
     * replace another fragment
     *
     * @param fragment that replaced
     */
    public fun showFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        ft.replace(R.id.mainFrame, fragment, "register")
        ft.commit()
    }

}
