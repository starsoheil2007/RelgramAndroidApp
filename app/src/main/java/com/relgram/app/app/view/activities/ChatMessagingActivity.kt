package com.relgram.app.app.view.activities

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.relgram.app.app.HamsanApp.Companion.context
import com.relgram.app.app.R
import com.relgram.app.app.database.AppDatabases
import com.relgram.app.app.databinding.ActivityChatMessagingBinding
import com.relgram.app.app.library.FontHelper
import com.relgram.app.app.library.Toaster
import com.relgram.app.app.models.BaseResponse
import com.relgram.app.app.view.adapters.ChatMessagingListAdapter
import com.relgram.app.app.view.dialog.LoadingDialog
import com.relgram.app.app.view.factories.MainViewModelFactory
import com.relgram.app.app.view_models.ChatMessagingActivityViewModel
import com.relgram.app.app.webservice.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.File

/**
 * chat message show activity
 *
 */
class ChatMessagingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatMessagingBinding
    private lateinit var viewModel: ChatMessagingActivityViewModel
    var chatId: Long = 0
    private lateinit var callRest: Disposable
    lateinit var layoutManager: LinearLayoutManager
    var runnable: Runnable? = null
    lateinit var handler: Handler
    var isBlockedChat = false
    private val PICK_IMAGE_REQUEST_CODE: Int = 100
    private val READ_EXTERNAL_STORAGE_REQUEST_CODE: Int = 101
    var imageFile: File? = null
    public var loading: LoadingDialog? = null

    companion object {
        const val CHAT_ID = "chatId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_messaging)
        viewModel = ViewModelProviders.of(this, MainViewModelFactory(this)).get(ChatMessagingActivityViewModel::class.java)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd = true
        binding.messageList.layoutManager = layoutManager

        initToolBar()
        loading = LoadingDialog.newInstance(resources.getString(R.string.loading))


        if (intent.extras.getLong(CHAT_ID, 0) != 0.toLong()) {
            chatId = intent.extras.getLong(CHAT_ID)
        }
        getChatList()
        binding.buttonChatboxSend.typeface = FontHelper.instance.getPersianTextTypeface()
        binding.buttonChatboxSend.setOnClickListener {
            if (!binding.buttonChatboxSend.text.toString().isNullOrEmpty()) {
                sendChat()
            }
        }

        handler = Handler()
        runnable = Runnable {
            getChatList()
        }
        binding.buttonPickImage.typeface = FontHelper.instance.getIconTypeface()
        binding.buttonChatboxSend.typeface = FontHelper.instance.getIconTypeface()
        binding.buttonPickImage.setOnClickListener {
            pickImage()
        }


    }

    /**
     * pick image from gallery for chat message
     *
     */
    private fun pickImage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_REQUEST_CODE)
        }
    }


    /**
     * block user question dialog show
     *
     */
    fun blockDialog() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(context.resources.getString(R.string.blockDialogTitle))
        alertDialog.setMessage(context.resources.getText(R.string.blockDialogText))
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.resources.getText(R.string.dialogNo)) { dialogInterface: DialogInterface, i: Int ->
            alertDialog.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.resources.getText(R.string.dialogYes)) { dialog, which ->
            blockRest()
        }
        alertDialog.show()
    }

    /**
     * unblock user question dialog show
     *
     */

    fun unBlockDialog() {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(context.resources.getString(R.string.unBlockDialogTitle))
        alertDialog.setMessage(context.resources.getText(R.string.unBlockDialogText))
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.resources.getText(R.string.dialogNo)) { dialogInterface: DialogInterface, i: Int ->
            alertDialog.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.resources.getText(R.string.dialogYes)) { dialog, which ->
            unBlockRest()
        }
        alertDialog.show()
    }

    /**
     * block user request to server send
     *
     */
    fun blockRest() {
        callRest = WebService().blockChat(chatId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    // Progrss Start
                }
                .doOnTerminate {
                    // Progress End
                }
                .subscribe({ result ->
                    if (result.isSuccess) {
                        if (result.errorCode == 100) {
                            Toaster.toast(this, R.string.blockSuccessfully)
                        } else {
                            Toaster.toast(this, R.string.blockByAnother)
                        }
                    }
                }, { error ->
                    print(error.message)
                })
    }

    /**
     * unblock user request to server
     *
     */
    fun unBlockRest() {
        callRest = WebService().blockChat(chatId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    // Progrss Start
                }
                .doOnTerminate {
                    // Progress End
                }
                .subscribe({ result ->
                    if (result.isSuccess) {
                        if (result.errorCode == 100) {
                            Toaster.toast(this, R.string.unBlockSuccessfully)
                        } else {
                            Toaster.toast(this, R.string.unBlockYouCant)
                        }
                    }
                }, { error ->
                    print(error.message)
                })
    }

    private fun initToolBar() {
        setSupportActionBar(binding.include.mainToolbar);
        if (getSupportActionBar() != null) getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
        binding.include.iconMessage.visibility = View.GONE
        binding.include.iconUser.visibility = View.VISIBLE
        binding.include.iconUser.text = resources.getText(R.string.iconBlock)
        binding.include.iconUser.setOnClickListener {
            if (isBlockedChat) {
                unBlockDialog()
            } else {
                blockDialog()
            }
        }
    }

    /**
     * get chat list from server
     *
     */
    fun getChatList() {
        if (::callRest.isInitialized && !callRest.isDisposed) {
            handler.postDelayed(runnable, 5000)
            return
        }

        callRest = WebService().getChat(chatId, 0).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    // Progrss Start
                }
                .doOnTerminate {
                    // Progress End
                }
                .subscribe({ result ->
                    if (result.isSuccess && result.item != null) {
                        isBlockedChat = result.item!!.isBlocked
                        binding.include.toolbarTitle.text = resources.getString(R.string.chatWith)
                        if (isBlockedChat) {
                            binding.include.iconUser.setTextColor(resources.getColor(R.color.colorCount))
                        } else {
                            binding.include.iconUser.setTextColor(resources.getColor(R.color.white))
                        }
                        val userId = AppDatabases.dbInstance!!.userInfoDao().get().serverId
                        var chatMessagingListAdapter = ChatMessagingListAdapter(this, userId!!)
                        binding.messageList.adapter = chatMessagingListAdapter
                        chatMessagingListAdapter.updatePostList(chatList = result.item!!.text)
                    }
                    handler.postDelayed(runnable, 5000)
                }, { error ->
                    print(error.message)
                })
    }

    override fun onStop() {
        super.onStop()
        if (::callRest.isInitialized && !callRest.isDisposed) callRest.dispose()
        if (::handler.isInitialized) handler.removeCallbacks(runnable)
    }

    /**
     * send new message to chat
     *
     */
    fun sendChat() {
        callRest = WebService().sendChat(chatId, binding.edittextChatbox.text.toString(), null).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    // Progrss Start
                }
                .doOnTerminate {
                    // Progress End
                }
                .subscribe({ result ->
                    if (result.isSuccess && result.item != null) {
                        getChatList()
                        binding.edittextChatbox.setText("")
                    }
                }, { error ->
                    try {
                        var errorResult = Gson().fromJson((error as HttpException).response().errorBody()!!.string(), BaseResponse::class.java)
                        if (errorResult.errorCode == 900) {
                            Toaster.toast(this, R.string.youNeedExpDate)
                            val intent = Intent(context, ChargeNeedActivity::class.java)
                            context!!.startActivity(intent)
                        } else if (errorResult.errorCode == 901) {
                            Toaster.toast(this, R.string.youAreNotActive)
                        } else if (errorResult.errorCode == 801) {
                            Toaster.toast(this, R.string.thisChatIsBlocked)
                        } else {
                            Toaster.toast(this, R.string.errorInSendChat)

                        }
                    } catch (e: Exception) {
                        Toaster.toast(this, R.string.errorInSendChat)
                    }
                })
    }

    fun sendImage() {
        callRest = WebService().sendChat(chatId, null, imageFile).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (!loading!!.isAdded) loading!!.show(supportFragmentManager, "dialog")
                }
                .doOnTerminate {
                    loading!!.dismiss()
                }
                .subscribe({ result ->
                    if (result.isSuccess && result.item != null) {
                        getChatList()
                        binding.edittextChatbox.setText("")
                    }
                }, { error ->
                    try {
                        var errorResult = Gson().fromJson((error as HttpException).response().errorBody()!!.string(), BaseResponse::class.java)
                        if (errorResult.errorCode == 900) {
                            Toaster.toast(this, R.string.youNeedExpDate)
                            val intent = Intent(context, ChargeNeedActivity::class.java)
                            context!!.startActivity(intent)
                        } else if (errorResult.errorCode == 901) {
                            Toaster.toast(this, R.string.youAreNotActive)
                        } else if (errorResult.errorCode == 801) {
                            Toaster.toast(this, R.string.thisChatIsBlocked)
                        } else {
                            Toaster.toast(this, R.string.errorInSendChat)

                        }
                    } catch (e: Exception) {
                        Toaster.toast(this, R.string.errorInSendChat)
                    }
                })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return
            }
            val uri = data?.data
            if (uri != null) {
                imageFile = uriToImageFile(uri)
                sendImage()
//                ImageLoaderHelper.displayCircleImage(this, binding, uri)
//                binding.profileAvatar.setImageURI(uri)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // pick image after request permission success
                    pickImage()
                }
            }
        }
    }

    private fun uriToImageFile(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val filePath = cursor.getString(columnIndex)
                cursor.close()
                return File(filePath)
            }
            cursor.close()
        }
        return null
    }

}
