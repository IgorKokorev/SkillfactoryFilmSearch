package film.search.filmsearch.utils

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import film.search.filmsearch.App
import film.search.filmsearch.Constants
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.data.entity.WatchLaterFilm
import film.search.filmsearch.domain.Interactor
import java.util.Calendar
import javax.inject.Inject

class AlarmService(val context: Context) {
    @Inject
    lateinit var interactor: Interactor
    init {
        App.instance.dagger.inject(this)
    }

    fun setFilmNotificationAlarm(film: Film) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        DatePickerDialog(
            context,
            { _, dpdYear, dpdMonth, dayOfMonth ->
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, pickerMinute ->
                        val pickedDateTime = Calendar.getInstance()
                        pickedDateTime.set(
                            dpdYear,
                            dpdMonth,
                            dayOfMonth,
                            hourOfDay,
                            pickerMinute,
                            0
                        )
                        val dateTimeInMillis = pickedDateTime.timeInMillis
                        createWatchLaterEvent(dateTimeInMillis, film)
                    }

                TimePickerDialog(
                    context,
                    timeSetListener,
                    currentHour,
                    currentMinute,
                    true
                ).show()

            },
            currentYear,
            currentMonth,
            currentDay
        ).show()
    }

    private fun createWatchLaterEvent(dateTimeInMillis: Long, film: Film) {
        if (dateTimeInMillis < System.currentTimeMillis()) {
            println("!!! ALARM SERVICE !!! Current time in millis: ${System.currentTimeMillis()} is after the time to set Watch Later Alarm: ${dateTimeInMillis}")
            return
        }
        // saving film and alarm time to db
        interactor.saveFilmToWatchLater(film, time = dateTimeInMillis)

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(film.title, null, context, WatchLaterReminderReceiver()::class.java)
        val bundle: Bundle = Bundle()
        bundle.putParcelable(Constants.FILM, film)
        intent.putExtra(Constants.BUNDLE, bundle)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            film.tmdbId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            dateTimeInMillis,
            pendingIntent
        )
    }

    fun deleteAlarm(watchLaterFilm: WatchLaterFilm) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(watchLaterFilm.title, null, context, WatchLaterReminderReceiver()::class.java)
        val bundle: Bundle = Bundle()
        bundle.putParcelable(Constants.FILM, watchLaterFilm)
        intent.putExtra(Constants.BUNDLE, bundle)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            watchLaterFilm.tmdbId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun editAlarm(watchLaterFilm: WatchLaterFilm) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(watchLaterFilm.title, null, context, WatchLaterReminderReceiver()::class.java)
        val bundle: Bundle = Bundle()
        bundle.putParcelable(Constants.FILM, Converter.watchLaterToFilm(watchLaterFilm))
        intent.putExtra(Constants.BUNDLE, bundle)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            watchLaterFilm.tmdbId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            watchLaterFilm.time,
            pendingIntent
        )
    }
}