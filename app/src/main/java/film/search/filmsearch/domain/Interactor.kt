package film.search.filmsearch.domain

import androidx.lifecycle.LiveData
import film.search.filmsearch.data.MainRepository
import film.search.filmsearch.data.PreferenceProvider
import film.search.filmsearch.data.Secret
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.data.tmbd.TmdbApi
import film.search.filmsearch.data.tmbd.TmdbResultsDto
import film.search.filmsearch.utils.Converter
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
    fun getFilmsFromApi(page: Int, callback: ApiCallback) {

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
                val list = Converter.convertApiListToFilmList(response.body()?.tmdbFilms)
                saveFilmsToDB(list)
                callback.onSuccess()
            }

            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                callback.onFailure()
            }
        })


    }

    // Working with local films db
    fun clearLocalFilmsDB() {
        repo.clearFilmsDB()
    }
    fun saveFilmsToDB(list: List<Film>) {
        repo.putFilmsToDb(list)
    }
    fun getFilmsFromDB(): LiveData<List<Film>> = repo.getAllFilmsFromDB()

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