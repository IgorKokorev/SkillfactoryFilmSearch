package film.search.filmsearch.viewmodel

import androidx.lifecycle.ViewModel
import film.search.filmsearch.App
import film.search.filmsearch.data.entity.WatchLaterFilm
import film.search.filmsearch.domain.Interactor
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class WatchLaterFragmentViewModel: ViewModel() {
    @Inject
    lateinit var interactor: Interactor
    val watchLaterFilmList: Observable<List<WatchLaterFilm>>                   // Observable with list of films for MainFragment RV from DB

    init {
        App.instance.dagger.inject(this)
        watchLaterFilmList = interactor.getWatchLaterFilms()
    }
}