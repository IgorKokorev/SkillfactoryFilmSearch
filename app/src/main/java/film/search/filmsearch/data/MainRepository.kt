package film.search.filmsearch.data

import film.search.filmsearch.data.DAO.FilmDao
import film.search.filmsearch.data.entity.Film
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.util.concurrent.Executors

// Responsible for interchanging data between ViewModels and DB
class MainRepository(private val filmDao: FilmDao) {

    fun putFilmsToDb(films: List<Film>?) {
//        Executors.newSingleThreadExecutor().execute {
            filmDao.insertAll(films)
//        }
    }

    fun getAllFilmsFromDB(): Flow<List<Film>> {
        return filmDao.getCachedFilms()
    }

    fun clearFilmsDB() {
        Executors.newSingleThreadExecutor().execute {
            filmDao.deleteAll()
        }
    }

    fun getFavouriteFilmsFromDB(): Flow<List<Film>> {
        return emptyFlow()
    }
}