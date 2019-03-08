package com.relgram.app.app.view.activities

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ActivityPaymentBinding
import com.relgram.app.app.view.adapters.PaymentListAdapter
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view_models.PaymentActivityViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * show pricing list
 *
 */
class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var viewModel: PaymentActivityViewModel
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: PaymentListAdapter
    private lateinit var callRest: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this)).get(PaymentActivityViewModel::class.java)
        binding.viewModel = viewModel
        binding.mainList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        initToolBar()
        getPaymentList()
    }

    private fun initToolBar() {
        setSupportActionBar(binding.include.mainToolbar);
        binding.include.toolbarTitle.text = resources.getString(R.string.buyCharge)
        if (getSupportActionBar() != null) getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
        binding.include.iconMessage.visibility = View.GONE
        binding.include.iconUser.visibility = View.GONE
    }

    /**
     * get available pricing list from server
     *
     */
    private fun getPaymentList() {
        callRest = WebService().getPaymentList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                .doOnTerminate {
                    binding.progressCircular.visibility = View.GONE
                }
                .subscribe({ result ->
                    if (::adapter.isInitialized) {
                        adapter.updatePostList(paymentList = result.listItems!!)
                    } else {
                        adapter = PaymentListAdapter(this)
                        adapter.updatePostList(paymentList = result.listItems!!)
                        binding.mainList.adapter = adapter
                    }
                }, { error -> print(error.message) })
    }

    /**
     * on stop check rest is calling or not
     * for handle exception
     *
     */
    override fun onStop() {
        super.onStop()
        if (::callRest.isInitialized && !callRest.isDisposed) callRest.dispose()
    }
}
