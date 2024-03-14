package film.search.filmsearch.data

import film.search.filmsearch.data.DAO.FavoriteFilmDao
import film.search.filmsearch.data.DAO.FilmDao
import film.search.filmsearch.data.DAO.WatchLaterFilmDao
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.data.entity.WatchLaterFilm
import film.search.filmsearch.utils.Converter
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.Executors

// Responsible for interchanging data between ViewModels and DB
class MainRepository(
    private val filmDao: FilmDao,
    private val favoriteFilmDao: FavoriteFilmDao,
    private val watchLaterFilmDao: WatchLaterFilmDao
) {

    fun saveFilmsToDb(films: List<Film>) {
        filmDao.insertAll(films)
    }

    fun getAllFilmsFromDB(): Observable<List<Film>> {
        return filmDao.getCachedFilms()
    }

    fun clearFilmsDB() {
        Executors.newSingleThreadExecutor().execute {
            filmDao.deleteAll()
        }
    }

    fun saveFilmToFavorites(film: Film) {
        Executors.newSingleThreadExecutor().execute {
            favoriteFilmDao.insert(Converter.filmToFavorite(film))
        }
    }

    fun deleteFilmFromFavorites(film: Film) {
        Executors.newSingleThreadExecutor().execute {
            favoriteFilmDao.deleteByTmdbId(film.tmdbId)
        }
    }

    fun getFavoriteFilmsFromDB(): Observable<List<Film>> {
        return favoriteFilmDao.getFavoriteFilms().map {
            Converter.favoriteListToFilmList(it)
        }
    }

    fun isFilmInFavorites(film: Film): Observable<Boolean> {
            return favoriteFilmDao.existsByTmdbId(film.tmdbId)
    }

    fun saveFilmToWatchLater(film: Film, time: Long) {
        saveWatchLater(Converter.filmToWatchLater(film, time))
    }

    fun deleteFilmFromWatchLater(film: WatchLaterFilm) {
        Executors.newSingleThreadExecutor().execute {
            watchLaterFilmDao.delete(film)
        }
    }

    fun getWatchLaterFilms(): Observable<List<WatchLaterFilm>> {
        return watchLaterFilmDao.getWatchLaterFilms()
    }

    fun deleteFilmFromWatchLaterByTmdbId(tmdbId: Int) {
        Executors.newSingleThreadExecutor().execute {
            watchLaterFilmDao.deleteByTmdbId(tmdbId)
        }
    }

    fun saveWatchLater(watchLaterFilm: WatchLaterFilm) {
        Executors.newSingleThreadExecutor().execute {
            watchLaterFilmDao.insert(watchLaterFilm)
        }
    }
}

