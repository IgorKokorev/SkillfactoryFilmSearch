package film.search.filmsearch

import android.app.Application
import film.search.filmsearch.data.MainRepository
import film.search.filmsearch.domain.Interactor

class App : Application() {
    lateinit var repo: MainRepository
    lateinit var interactor: Interactor

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
