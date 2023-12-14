package film.search.filmsearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import film.search.filmsearch.App
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.domain.Interactor
import javax.inject.Inject

// ViewModel for FavoritesFragment
class FavoritesFragmentViewModel : ViewModel() {
    val filmsListLiveData = MutableLiveData<List<Film>>()
    @Inject
    lateinit var interactor: Interactor
    init {
        App.instance.dagger.inject(this)
        interactor.getFilmsFromApi(1, object : MainFragmentViewModel.ApiCallback {
            override fun onSuccess(films: List<Film>) {
                filmsListLiveData.postValue(films)
            }
            override fun onFailure() {
            }
        })
    }

}
