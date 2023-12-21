package film.search.filmsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import film.search.filmsearch.App
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.domain.Interactor
import film.search.filmsearch.utils.SingleLiveEvent
import javax.inject.Inject

class MainFragmentViewModel(state: SavedStateHandle) : ViewModel() {
    @Inject
    lateinit var interactor: Interactor

    val filmsListLiveData: LiveData<List<Film>>             // LiveData with list of films for MainFragment RV
    val apiErrorEvent = SingleLiveEvent<String>()           // SingleLiveEvent to show API errors
    private var toLoadFromAPI = true                        // Do we need to download data from API
    private var page = 0                                    // what page is to download from API
    private val savedStateHandle = state                    // To save state of the VM
    val showProgressBar: MutableLiveData<Boolean> =
        MutableLiveData() // Do we have to show ProgressBar (when data is loading)

    init {
        App.instance.dagger.inject(this)
        filmsListLiveData = interactor.getFilmsFromDB()
        loadFirstPage()
    }

    fun addNextPage() {
        if (!toLoadFromAPI) return  // Add next page only if we download data from API

        showProgressBar.postValue(true) // Starting loading films from API - show ProgressBar
        interactor.getFilmsFromApi(++page, object : Interactor.ApiCallback {
            override fun onSuccess() {
                showProgressBar.postValue(false) // hide ProgressBar
            }

            override fun onFailure() {
                apiErrorEvent.postValue("MainFragment")
                showProgressBar.postValue(false) // hide ProgressBar
                page--
            }
        })
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

