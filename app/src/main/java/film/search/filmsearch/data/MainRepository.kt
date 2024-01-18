package film.search.filmsearch.data

import film.search.filmsearch.data.DAO.FilmDao
import film.search.filmsearch.data.entity.Film
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.Executors

// Responsible for interchanging data between ViewModels and DB
class MainRepository(private val filmDao: FilmDao) {

    fun putFilmsToDb(films: List<Film>) {
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

}