package com.relgram.app.app

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.relgram.app.app.database.AppDatabases
import com.relgram.app.app.database.Notifications
import com.relgram.app.app.models.NotificationModels
import com.relgram.app.app.view.activities.SplashActivity
import org.json.JSONObject

class RelGramFireBaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        super.onNewToken(token)

        HamsanApp.checkForUserDetailsUpdate()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        val params = remoteMessage!!.getData()
        val json = JSONObject(params)
        val notificationModels = Gson().fromJson<NotificationModels>(json.toString(), NotificationModels::class.java!!)
        AppDatabases.dbInstance!!.notificationDao().insert(notifications = Notifications(null, notificationModels.title, notificationModels.text))
        sendNotification(notificationModels.title!!, notificationModels.text!!)
    }


    private fun sendNotification(title: String, body: String) {
        //RemoteMessage.Notification notification = remoteMessage.getNotification();
        val intent: Intent = Intent(this, SplashActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, 777, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(this)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        builder.setSound(notificationSound)
        val noti = builder.build()
        noti.flags = Notification.FLAG_AUTO_CANCEL

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, noti)
    }
}