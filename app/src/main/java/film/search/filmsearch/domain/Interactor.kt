package film.search.filmsearch.domain

import film.search.filmsearch.data.MainRepository
import film.search.filmsearch.data.PreferenceProvider
import film.search.filmsearch.data.Secret
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.data.tmbd.TmdbApi
import film.search.filmsearch.data.tmbd.TmdbResultsDto
import film.search.filmsearch.utils.Converter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

// class to interact with film db, external API and preferences
class Interactor(
    private val repo: MainRepository,
    private val retrofitService: TmdbApi,
    private val preferences: PreferenceProvider
) {
    var progressBarState = Channel<Boolean>(Channel.CONFLATED)
    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    fun getFilmsFromApi(page: Int, callback: ApiCallback) {
        // Show ProgressBar
        scope.launch {
            progressBarState.send(true)
        }

        retrofitService.getFilms(
            getDefaultCategoryFromPreferences(),
            Secret.KEY,
            Locale.getDefault().language,
            page
        ).enqueue(object :
            Callback<TmdbResultsDto> {
            override fun onResponse(
                call: Call<TmdbResultsDto>,
                response: Response<TmdbResultsDto>
            ) {
                scope.launch {
                    // Convert API response to list of films using flow
                    val list = response.body()?.tmdbFilms?.asFlow()?.map { Converter.convertApiToFilm(it) }?.toList()
                    saveFilmsToDB(list)
                    progressBarState.send(false)
                }

                callback.onSuccess()
            }

            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                scope.launch {
                    progressBarState.send(false)
                }
                callback.onFailure()
            }
        })


    }

    // Working with local films db
    fun clearLocalFilmsDB() {
        repo.clearFilmsDB()
    }
    fun saveFilmsToDB(list: List<Film>?) {
        repo.putFilmsToDb(list)
    }
    fun getFilmsFromDB(): Flow<List<Film>> = repo.getAllFilmsFromDB()

    fun getFavouriteFilmsFromDB(): Flow<List<Film>> = repo.getFavouriteFilmsFromDB()

    // Working with default films category
    fun saveDefaultCategoryToPreferences(category: String) {
        preferences.saveDefaultCategory(category)
    }

    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()
    // Last external API request time
    fun saveLastAPIRequestTime() {
        preferences.saveLastAPIRequestTime()
    }

    fun getLastAPIRequestTime() = preferences.getLastAPIRequestTime()
    // Films category saved in local DB
    fun saveCategoryInDB(category: String) {
        preferences.saveCategoryInDB(category)
    }
    fun getCategoryInDB() = preferences.getCategoryInDB()

    interface ApiCallback {
        fun onSuccess()
        fun onFailure()
    }


}