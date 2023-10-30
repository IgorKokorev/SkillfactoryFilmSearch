package film.search.filmsearch

import android.app.Application
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import film.search.filmsearch.data.MainRepository
import film.search.filmsearch.domain.Interactor
import film.search.filmsearch.utils.NotificationService

class App : Application() {
    lateinit var repo: MainRepository
    lateinit var interactor: Interactor
    lateinit var notificationService: NotificationService
    val TRANSITION_NAME = "transition"
    val TIME_INTERVAL = 2000L
    val FILM = "film"
    val POSITION = "position"
    val POSTER = "poster"
    val DESCRIPTION = "description"
    val FRAGMENT_TAG = "tag"

    override fun onCreate() {
        super.onCreate()
        instance = this
        repo = MainRepository(this)
        interactor = Interactor(repo)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
