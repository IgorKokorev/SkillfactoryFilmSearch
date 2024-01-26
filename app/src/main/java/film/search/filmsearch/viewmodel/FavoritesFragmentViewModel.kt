package film.search.filmsearch.viewmodel

import androidx.lifecycle.ViewModel
import film.search.filmsearch.App
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.domain.Interactor
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

// ViewModel for FavoritesFragment
class FavoritesFragmentViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor
    val filmsList: Observable<List<Film>>                   // Observable with list of films for MainFragment RV from DB

    init {
        App.instance.dagger.inject(this)
        filmsList = interactor.getFavouriteFilmsFromDB()
    }

}
