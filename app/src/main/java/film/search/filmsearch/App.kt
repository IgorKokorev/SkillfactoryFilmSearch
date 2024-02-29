package film.search.filmsearch

import android.app.Application
import film.search.filmsearch.di.AppComponent
import film.search.filmsearch.di.DaggerAppComponent
import film.search.filmsearch.di.modules.DatabaseModule
import film.search.filmsearch.di.modules.DomainModule
import film.search.retrofit.DaggerRemoteComponent

class App : Application() {
    lateinit var dagger: AppComponent
    val FRAGMENT_TAG = "fragment"
    val BACK_CLICK_TIME_INTERVAL = 2000L
    val API_REQUEST_TIME_INTERVAL = 1000L * 60 * 10
    val BOTTOM_MENU_ITEMS_NUMBER = 4
    val BUNDLE: String = "bundle"
    val FILM = "film"
    val TRANSITION_NAME = "transition"

    override fun onCreate() {
        super.onCreate()
        instance = this
        //Создаем компонент
        dagger = DaggerAppComponent.builder()
            .remoteProvider(DaggerRemoteComponent.create())
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
