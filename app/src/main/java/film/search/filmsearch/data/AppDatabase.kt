package film.search.filmsearch.data

import androidx.room.Database
import androidx.room.RoomDatabase
import film.search.filmsearch.data.DAO.FilmDao
import film.search.filmsearch.data.entity.Film

@Database(entities = [Film::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}