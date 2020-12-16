package com.strydhr.thepasar.Services

import android.R
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.strydhr.thepasar.Controller.LoginActivity
import java.util.*

class FirebaseMsgServices: FirebaseMessagingService() {
    private val ADMIN_CHANNEL_ID = "admin_channel"
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val intent = Intent(this, LoginActivity::class.java)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID: Int = Random().nextInt(3000)
        /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel
      */if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            setupChannels(notificationManager)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val largeIcon = BitmapFactory.decodeResource(
            resources,
            R.drawable.btn_dropdown
        )
        val notificationSoundUri: Uri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this,ADMIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.arrow_down_float)
            .setLargeIcon(largeIcon)
            .setContentTitle(remoteMessage.data["title"])
            .setContentText(remoteMessage.data["message"])
            .setAutoCancel(true)
            .setSound(notificationSoundUri)
            .setContentIntent(pendingIntent)
        //Set notification color to match your app color template
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(resources.getColor(R.color.holo_green_light))
        }
        notificationManager.notify(notificationID, notificationBuilder.build())
    }
}
