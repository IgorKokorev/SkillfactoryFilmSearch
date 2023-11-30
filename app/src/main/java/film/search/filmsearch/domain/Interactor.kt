package film.search.filmsearch.domain

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
class Interactor(private val repo: MainRepository, private val retrofitService: TmdbApi) {
    fun getFilmsFromApi(page: Int, callback: MainFragmentViewModel.ApiCallback) {
        retrofitService.getFilms(Secret.KEY, Locale.getDefault().language, page).enqueue(object :
            Callback<TmdbResultsDto> {
            override fun onResponse(call: Call<TmdbResultsDto>, response: Response<TmdbResultsDto>) {
                callback.onSuccess(Converter.convertApiListToDtoList(response.body()?.tmdbFilms))
            }
            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                callback.onFailure()
            }
        })
    }
}