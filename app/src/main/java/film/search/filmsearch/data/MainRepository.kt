package film.search.filmsearch.data

import film.search.filmsearch.data.DAO.FilmDao
import film.search.filmsearch.data.entity.Film
import java.util.concurrent.Executors

// Responsible for interchanging data between ViewModels and DB
class MainRepository(private val filmDao: FilmDao) {

    fun putFilmsToDb(films: List<Film>) {
        Executors.newSingleThreadExecutor().execute {
            filmDao.insertAll(films)
        }
    }

    fun getAllFilmsFromDB(): List<Film> {
        return filmDao.getCachedFilms()
    }

    fun clearFilmsDB() {
        Executors.newSingleThreadExecutor().execute {
            filmDao.deleteAll(filmDao.getCachedFilms())
        }
    }
}