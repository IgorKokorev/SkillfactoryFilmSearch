package film.search.filmsearch

import android.app.Application
import film.search.filmsearch.di.AppComponent
import film.search.filmsearch.di.DaggerAppComponent

class App : Application() {
    lateinit var dagger: AppComponent
    val FRAGMENT_TAG = "fragment"
    val TIME_INTERVAL = 2000L
    val FILM = "film"
    val POSITION = "position"
    val TRANSITION_NAME = "transition"

    override fun onCreate() {
        super.onCreate()
        instance = this
        //Создаем компонент
        dagger = DaggerAppComponent.create()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
