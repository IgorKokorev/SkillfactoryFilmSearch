package film.search.filmsearch.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import film.search.filmsearch.App
import film.search.filmsearch.Constants
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.domain.Interactor
import javax.inject.Inject

class WatchLaterReminderReceiver : BroadcastReceiver() {
    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle: Bundle? = intent?.getBundleExtra(Constants.BUNDLE)
        val film =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle?.getParcelable(Constants.FILM, Film::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle?.getParcelable<Film>(Constants.FILM)
            }
        if (film != null) {
            interactor.deleteFilmFromWatchLaterByTmdbId(film.tmdbId)
            NotificationService(context!!).sendFilmNotification(film!!)
        }
    }
}