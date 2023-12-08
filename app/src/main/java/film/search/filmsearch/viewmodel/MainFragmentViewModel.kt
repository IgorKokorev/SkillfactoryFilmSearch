package film.search.filmsearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import film.search.filmsearch.App
import film.search.filmsearch.domain.Film
import film.search.filmsearch.domain.Interactor
import javax.inject.Inject

class MainFragmentViewModel : ViewModel() {
    val filmsListLiveData = MutableLiveData<List<Film>>()

    @Inject
    lateinit var interactor: Interactor
    private var page = 0

    init {
        App.instance.dagger.inject(this)
        loadFirstPage()
    }

    interface ApiCallback {
        fun onSuccess(films: List<Film>)
        fun onFailure()
    }

    fun addNextPage() {
        interactor.getFilmsFromApi(++page, object : ApiCallback {
            override fun onSuccess(films: List<Film>) {
                filmsListLiveData.value = filmsListLiveData.value?.plus(films) ?: films
            }

            override fun onFailure() {
                filmsListLiveData.postValue(interactor.getFilmsFromDB())
                page--
            }
        })
    }

    fun loadFirstPage() {
        filmsListLiveData.value = emptyList()
        page = 0
        addNextPage()
    }
}
