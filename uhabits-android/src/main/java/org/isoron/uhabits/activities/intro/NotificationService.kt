package org.isoron.uhabits.activities.intro

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import java.util.*
import android.app.NotificationChannel
import org.isoron.uhabits.R
import org.isoron.uhabits.activities.habits.list.ListHabitsActivity


class NotificationService : IntentService("NotificationService") {
    private lateinit var mNotification: Notification
    private val mNotificationId: Int = 1000

    @SuppressLint("NewApi")
    private fun createChannel() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Crearea canalului de notificari

            val context = this.applicationContext
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.parseColor("#e8334a")
            notificationChannel.description = getString(R.string.notification_channel_description)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    companion object {

        const val CHANNEL_ID = "samples.notification.devdeeds.com.CHANNEL_ID"
        const val CHANNEL_NAME = "Habits Notification"
    }


    override fun onHandleIntent(intent: Intent?) {

        //Crearea canalului de notificari
        createChannel()
        var timestamp: Long = 0
        if (intent != null && intent.extras != null) {
            timestamp = intent.extras!!.getLong("timestamp")
        }

        if (timestamp > 0) {

            val context = this.applicationContext
            val notifyIntent = Intent(this, ListHabitsActivity::class.java)

            val title = "Notificare uhabits"
            val message = "Hi there! How about setting a new habit? Sounds like fun?"

            notifyIntent.putExtra("title", title)
            notifyIntent.putExtra("message", message)
            notifyIntent.putExtra("notification", true)

            notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp

            val pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val res = this.resources
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                mNotification = Notification.Builder(this, CHANNEL_ID)
                    // Setarea intent-ului care va porni cand utilizatorul apasa pe notificare
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(Notification.BigTextStyle()
                        .bigText(message))
                    .setContentText(message).build()
            } else {
                mNotification = Notification.Builder(this)
                    // Setarea intent-ului care va porni cand utilizatorul apasa pe notificare
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle(title)
                    .setStyle(Notification.BigTextStyle()
                        .bigText(message))
                    .setSound(uri)
                    .setContentText(message).build()
            }

            var notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(mNotificationId, mNotification)
        }
    }
}