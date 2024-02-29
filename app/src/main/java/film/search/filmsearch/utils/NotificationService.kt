package film.search.filmsearch.utils

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.content.ContextCompat.getSystemService
import film.search.filmsearch.App
import film.search.filmsearch.R
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.view.MainActivity
import java.util.Calendar

// Service for push notifications
class NotificationService(val context: Context) {
    private val CHANNEL_ID = "FilmSearch Notification"
    private val CHANNEL_NAME = "Film Search"
    private val CHANNEL_DESCRIPTION = "FilmSearch Notification channel"
    private val filmNotificationTitle = getString(context, R.string.notification_title)
    private val filmNotificationText = getString(context, R.string.notification_text)
    private val importance = NotificationManager.IMPORTANCE_DEFAULT
    private var notificationManager: NotificationManager? = null
    private val icon: Int = R.drawable.icon_movie

    init {
        notificationManager = getSystemService(context, NotificationManager::class.java)

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            // Register the channel with the system
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun sendNotification(id: Int, title: String, text: String, pendingIntent: PendingIntent) {
        val notification: Notification

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(text)
//                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setContentIntent(pendingIntent)
                .setShowWhen(true)
                .setAutoCancel(true)
//            .setTimeoutAfter(5000L)
                .setOnlyAlertOnce(true)
                .build()
        } else {
            notification = Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(icon)
                .setContentIntent(pendingIntent)
                .setShowWhen(true)
                .setAutoCancel(true)
                .build()
        }

        notificationManager?.notify(id, notification)
    }

    fun sendFilmNotification(film: Film) {

        val id = 1
        var dateTimeInMillis: Long = 0L



        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(App.instance.FILM, film)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        sendNotification(id, filmNotificationTitle, filmNotificationText + film.title, pendingIntent)
    }


}
