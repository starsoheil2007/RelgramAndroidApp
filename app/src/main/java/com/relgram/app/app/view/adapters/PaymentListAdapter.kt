package com.relgram.app.app.view.adapters

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.relgram.app.app.R
import com.relgram.app.app.databinding.ListRowPaymentBinding
import com.relgram.app.app.models.PaymentListResponse
import com.relgram.app.app.view.dialog.LoadingDialog
import com.relgram.app.app.view_models.ListPaymentViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PaymentListAdapter(var context: Context) : RecyclerView.Adapter<PaymentListAdapter.ViewHolder>() {

    private var paymentList = ArrayList<PaymentListResponse>()
    lateinit var binding: ListRowPaymentBinding
    public var loading: LoadingDialog = LoadingDialog.newInstance(context.resources.getString(R.string.loading))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_row_payment, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(paymentList[position])
        binding.root.setOnClickListener {
            startNewPayment(paymentList[position].id)
        }

    }

    override fun getItemCount(): Int {
        return paymentList.size
    }

    fun updatePostList(paymentList: List<PaymentListResponse>) {
        this.paymentList.addAll(paymentList)
        notifyDataSetChanged()
    }


    class ViewHolder(private val binding: ListRowPaymentBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ListPaymentViewModel()
        fun bind(payment: PaymentListResponse) {
            viewModel.bind(payment)
            binding.viewModel = viewModel
        }
    }


    fun startNewPayment(id: Int) {
        var callRest = WebService().getPayment(id).subscribeOn(Schedulers.io())
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
                            if (result.isSuccess && result.item != null) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.item!!.url + result.item!!.Id))
                                context.startActivity(intent)
                            }
                        },
                        { error -> print(error.message) }
                )
    }
}