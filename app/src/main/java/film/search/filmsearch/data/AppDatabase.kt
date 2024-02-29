package film.search.filmsearch.data

import androidx.room.Database
import androidx.room.RoomDatabase
import film.search.filmsearch.data.DAO.FavoriteFilmDao
import film.search.filmsearch.data.DAO.FilmDao
import film.search.filmsearch.data.DAO.WatchLaterFilmDao
import film.search.filmsearch.data.entity.FavoriteFilm
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.data.entity.WatchLaterFilm

@Database(entities = [Film::class, FavoriteFilm::class, WatchLaterFilm::class], version = 6, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
    abstract fun favoriteFilmsDao(): FavoriteFilmDao
    abstract fun watchLaterFilmDao(): WatchLaterFilmDao
}