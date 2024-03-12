package film.search.filmsearch.domain

import film.search.filmsearch.data.MainRepository
import film.search.filmsearch.data.PreferenceProvider
import film.search.filmsearch.data.Secret
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.data.entity.WatchLaterFilm
import film.search.filmsearch.utils.Converter
import film.search.retrofit.TmdbApi
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.Locale

// class to interact with film db, external API and preferences
class Interactor(
    private val repo: MainRepository,
    private val retrofitService: TmdbApi,
    private val preferences: PreferenceProvider
) {
    var progressBarState: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private var favoritesTmdbId = emptyList<Int>()

    init {
        getFavouriteFilmsFromDB()
            .subscribeOn(Schedulers.io())
            .map {
                val ids: MutableList<Int> = mutableListOf<Int>()

                it.forEach { film -> ids.add(film.tmdbId) }
                ids
            }
            .subscribe {
                favoritesTmdbId = it
            }
    }
    fun getFilmsFromApi(page: Int) {
        // Show ProgressBar
        progressBarState.onNext(true)

        // Switch to RxJava observable. We don't about dispose it as Interactor is singleton
        retrofitService.getFilms(
            getDefaultCategoryFromPreferences(),
            Secret.KEY,
            Locale.getDefault().language,
            page
        )
            .map { dto ->
                Converter.convertApiListToFilmList(dto.tmdbFilms)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe { list ->
                saveFilmsToDB(list)
                progressBarState.onNext(false)
            }
    }

    fun searchFilmsFromApi(query: String, page: Int) {
        // Show ProgressBar
        progressBarState.onNext(true)

        // Search films by string from API
        retrofitService.searchFilms(
            query,
            Secret.KEY,
            Locale.getDefault().language,
            page
        )
            .doOnError { t -> null }
            .map { dto ->
                Converter.convertApiListToFilmList(dto.tmdbFilms)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe { list ->
                saveFilmsToDB(list)
                progressBarState.onNext(false)
            }
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
        list.forEach {
            it.isFavorite = favoritesTmdbId.contains(it.tmdbId)
        }
        repo.saveFilmsToDb(list)
    }

    fun getFilmsFromDB(): Observable<List<Film>> = repo.getAllFilmsFromDB()

    // Working with local DB of favorite films
    fun getFavouriteFilmsFromDB(): Observable<List<Film>> = repo.getFavoriteFilmsFromDB()

    fun saveFilmToFavorites(film: Film) {
        repo.saveFilmToFavorites(film)
    }

    fun deleteFilmFromFavorites(film: Film) {
        repo.deleteFilmFromFavorites(film)
    }

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

    fun isFavorite(film: Film): Observable<Boolean> {
        return repo.isFilmInFavorites(film)
    }

    fun saveFilmToWatchLater(film: Film, time: Long) {
        repo.saveFilmToWatchLater(film, time)
    }

    fun deleteFilmFromWatchLater(film: WatchLaterFilm) {
        repo.deleteFilmFromWatchLater(film)
    }

    fun getWatchLaterFilms(): Observable<List<WatchLaterFilm>> {
        return repo.getWatchLaterFilms()
    }

    fun deleteFilmFromWatchLaterByTmdbId(tmdbId: Int) {
        repo.deleteFilmFromWatchLaterByTmdbId(tmdbId)
    }

    fun saveWatchLater(watchLaterFilm: WatchLaterFilm) {
        repo.saveWatchLater(watchLaterFilm)

    }

    fun saveFistFreeAccessTime() {
        preferences.saveFistFreeAccessTime()
    }
    fun getFistFreeAccessTime() = preferences.getFirstFreeAccessTime()
}