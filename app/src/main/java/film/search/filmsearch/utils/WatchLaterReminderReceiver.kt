package film.search.filmsearch.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import film.search.filmsearch.App
import film.search.filmsearch.data.entity.Film

class WatchLaterReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val film =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent?.getParcelableExtra(App.instance.FILM, Film::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent?.getParcelableExtra(App.instance.FILM) as Film?
            }

        NotificationService(context!!).sendFilmNotification(film!!)
    }
}