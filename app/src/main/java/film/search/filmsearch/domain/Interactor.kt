package film.search.filmsearch.domain

import film.search.filmsearch.data.MainRepository
import film.search.filmsearch.data.PreferenceProvider
import film.search.filmsearch.data.Secret
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.data.tmbd.TmdbApi
import film.search.filmsearch.data.tmbd.TmdbResultsDto
import film.search.filmsearch.utils.Converter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    fun getFilmsFromApi(page: Int) {
        // Show ProgressBar
        progressBarState.onNext(true)

        retrofitService.getFilms(
            getDefaultCategoryFromPreferences(),
            Secret.KEY,
            Locale.getDefault().language,
            page
        )
            .enqueue(object :
                Callback<TmdbResultsDto> {
            override fun onResponse(
                call: Call<TmdbResultsDto>,
                response: Response<TmdbResultsDto>
            ) {
                Completable.fromSingle<List<Film>> {
                    val list = try {
                    Converter.convertApiListToFilmList(response.body()?.tmdbFilms)
                } catch (e: Exception) {
                    emptyList()
                }
                    saveFilmsToDB(list)
                }
                    .subscribeOn(Schedulers.io())
                    .subscribe()
                progressBarState.onNext(false)
            }

            override fun onFailure(call: Call<TmdbResultsDto>, t: Throwable) {
                progressBarState.onNext(false)
            }
        })


    }

    // Working with local films db
    fun clearLocalFilmsDB() {
        repo.clearFilmsDB()
    }
    fun clearListAfterCategoryChange() {
        Completable.fromSingle<List<Film>> {
            repo.clearFilmsDB()
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun saveFilmsToDB(list: List<Film>) {
        repo.putFilmsToDb(list)
    }

    fun getFilmsFromDB(): Observable<List<Film>> = repo.getAllFilmsFromDB()

//    fun getFavouriteFilmsFromDB(): Flow<List<Film>> = repo.getFavouriteFilmsFromDB()

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
}