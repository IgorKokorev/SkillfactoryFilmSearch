package film.search.filmsearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import film.search.filmsearch.App
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.domain.Interactor
import java.util.concurrent.Executors
import javax.inject.Inject

class MainFragmentViewModel(state: SavedStateHandle) : ViewModel() {
    @Inject
    lateinit var interactor: Interactor

    val filmsListLiveData = MutableLiveData<List<Film>>()   // LiveData with list of films for MainFragment RV
    private var toLoadFromAPI = true                        // Do we need to download data from API
    private var page = 0                                    // what page is to download from API
    private val savedStateHandle = state                    // To save state of the VM

    init {
        App.instance.dagger.inject(this)
        loadFirstPage()
    }

    fun addNextPage() {
        if (!toLoadFromAPI) return  // Add next page only if we download data from API

        interactor.getFilmsFromApi(++page, object : Interactor.ApiCallback {
            override fun onSuccess(films: List<Film>) {
                interactor.saveFilmsToDB(films)
                filmsListLiveData.value = filmsListLiveData.value?.plus(films) ?: films
            }

            override fun onFailure() {
                Executors.newSingleThreadExecutor().execute {
                    filmsListLiveData.postValue(interactor.getFilmsFromDB())
                    page--
                }
            }
        })
    }

    fun loadFirstPage() {
        val currentTime = System.currentTimeMillis()
        val savedTime = interactor.getLastAPIRequestTime()

        filmsListLiveData.value = emptyList()
        page = 0

        // if more than established time passed since last API call or category has been changed than request API again
        if (
            (currentTime - savedTime) > App.instance.API_REQUEST_TIME_INTERVAL ||
            interactor.getDefaultCategoryFromPreferences() != interactor.getCategoryInDB()
            ) {
            toLoadFromAPI = true
            interactor.clearLocalFilmsDB()
            interactor.saveCategoryInDB(interactor.getDefaultCategoryFromPreferences())
            interactor.saveLastAPIRequestTime()

            addNextPage()
        } else { // else get films from db
            toLoadFromAPI = false
            Executors.newSingleThreadExecutor().execute {
                filmsListLiveData.postValue(interactor.getFilmsFromDB())
            }
        }

    }
}
