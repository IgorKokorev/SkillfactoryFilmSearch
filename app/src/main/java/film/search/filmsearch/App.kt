package film.search.filmsearch

import android.app.Application
import film.search.filmsearch.di.AppComponent
import film.search.filmsearch.di.DaggerAppComponent
import film.search.filmsearch.di.modules.DatabaseModule
import film.search.filmsearch.di.modules.DomainModule
import film.search.filmsearch.di.modules.RemoteModule

class App : Application() {
    lateinit var dagger: AppComponent
    val FRAGMENT_TAG = "fragment"
    val BACK_CLICK_TIME_INTERVAL = 2000L
    val API_REQUEST_TIME_INTERVAL = 1000L * 60 * 10
    val FILM = "film"
    val POSITION = "position"
    val TRANSITION_NAME = "transition"

    override fun onCreate() {
        super.onCreate()
        instance = this
        //Создаем компонент
        dagger = DaggerAppComponent.builder()
            .remoteModule(RemoteModule())
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
