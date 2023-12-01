package film.search.filmsearch.domain

import film.search.filmsearch.data.Secret
import film.search.filmsearch.data.tmbd.TmdbApi
import film.search.filmsearch.data.tmbd.TmdbResultsDto
import film.search.filmsearch.di.modules.FilmRepositoryInterface
import film.search.filmsearch.di.modules.InteractorInterface
import film.search.filmsearch.utils.Converter
import film.search.filmsearch.viewmodel.MainFragmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale
import javax.inject.Inject

// class to interact with film db
class Interactor @Inject constructor(private val repo: FilmRepositoryInterface, private val retrofitService: TmdbApi) : InteractorInterface {
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