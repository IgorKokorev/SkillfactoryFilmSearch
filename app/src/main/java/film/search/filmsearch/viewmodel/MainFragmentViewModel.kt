package film.search.filmsearch.viewmodel

import androidx.lifecycle.ViewModel
import film.search.filmsearch.App
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.domain.Interactor
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class MainFragmentViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor

    val filmsList: Observable<List<Film>>                   // Observable with list of films for MainFragment RV from DB
    val showProgressBar: BehaviorSubject<Boolean>           // Do we have to show ProgressBar (when data is loading)
    private var toLoadFromAPI = true                        // Do we need to download data from API
    private var page = 0                                    // what page is to download from API

    init {
        App.instance.dagger.inject(this)
        showProgressBar = interactor.progressBarState
        filmsList = interactor.getFilmsFromDB()
        loadFirstPage()
    }

    fun addNextPage() {
        if (!toLoadFromAPI) return  // Add next page only if we download data from API
        interactor.getFilmsFromApi(++page)
    }

    fun loadFirstPage() {
        val currentTime = System.currentTimeMillis()
        val savedTime = interactor.getLastAPIRequestTime()

        page = 0

        // if more than established time passed since last API call or category has been changed than request API again
        if ((currentTime - savedTime) > App.instance.API_REQUEST_TIME_INTERVAL ||
            interactor.getDefaultCategoryFromPreferences() != interactor.getCategoryInDB()
        ) {
            toLoadFromAPI = true
            interactor.clearLocalFilmsDB()
            interactor.saveCategoryInDB(interactor.getDefaultCategoryFromPreferences())
            interactor.saveLastAPIRequestTime()

            addNextPage()
        } else { // else get films from db
            toLoadFromAPI = false
        }

    }
}

