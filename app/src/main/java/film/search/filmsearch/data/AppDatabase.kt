package film.search.filmsearch.data

import androidx.room.Database
import androidx.room.RoomDatabase
import film.search.filmsearch.data.DAO.FavoriteFilmDao
import film.search.filmsearch.data.DAO.FilmDao
import film.search.filmsearch.data.entity.FavoriteFilm
import film.search.filmsearch.data.entity.Film

@Database(entities = [Film::class, FavoriteFilm::class], version = 4, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
    abstract fun favoriteFilmsDao(): FavoriteFilmDao
}