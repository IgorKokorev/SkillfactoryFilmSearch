package film.search.filmsearch.viewmodel

import androidx.lifecycle.ViewModel
import film.search.filmsearch.App
import film.search.filmsearch.domain.Interactor
import javax.inject.Inject

// ViewModel for FavoritesFragment
class FavoritesFragmentViewModel : ViewModel() {
//    val filmsList: Flow<List<Film>>
    @Inject
    lateinit var interactor: Interactor
    init {
        App.instance.dagger.inject(this)
//        filmsList = interactor.getFavouriteFilmsFromDB()
    }

}
