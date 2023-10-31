package film.search.filmsearch.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

// Service for push notifications
class NotificationService(val context: Context) {
    val CHANNEL_ID = "filmNotification"
    val name = "Film status"
    val descriptionText = "Your film status"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val priority = NotificationCompat.PRIORITY_HIGH // for compatibility with Android below v.8
    var notificationManager: NotificationManager? = null
    init {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            notificationManager =
                getSystemService(context, NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun sendNotification(icon: Int, title: String, text: String/*, pendingIntent: PendingIntent*/) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(priority) // for compatibility with Android below v.8
//            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
//            .setTimeoutAfter(2000L)
            .setOnlyAlertOnce(true)
            .build()

        notificationManager?.notify(1, notification)
    }
}
