package film.search.filmsearch.domain

import film.search.filmsearch.data.MainRepository
import film.search.filmsearch.data.PreferenceProvider
import film.search.filmsearch.data.Secret
import film.search.filmsearch.data.tmbd.TmdbApi
import film.search.filmsearch.data.tmbd.TmdbResultsDto
import film.search.filmsearch.utils.Converter
import film.search.filmsearch.viewmodel.MainFragmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

// class to interact with film db
class Interactor(
    private val repo: MainRepository,
    private val retrofitService: TmdbApi,
    private val preferences: PreferenceProvider) {
    fun getFilmsFromApi(page: Int, callback: MainFragmentViewModel.ApiCallback) {
        retrofitService.getFilms(getDefaultCategoryFromPreferences(), Secret.KEY, Locale.getDefault().language, page).enqueue(object :
            Callback<TmdbResultsDto> {
            override fun onResponse(call: Call<TmdbResultsDto>, response: Response<TmdbResultsDto>) {
                callback.onSuccess(Converter.convertApiListToFilmList(response.body()?.tmdbFilms))
            }
            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                callback.onFailure()
            }
        })
    }

    fun saveDefaultCategoryToPreferences(category: String) {
        preferences.saveDefaultCategory(category)
    }
    fun getDefaultCategoryFromPreferences() = preferences.getDefaultCategory()
}