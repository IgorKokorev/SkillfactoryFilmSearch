package film.search.filmsearch.data.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import film.search.filmsearch.data.entity.Film
import kotlinx.coroutines.flow.Flow

// Room's DAO interface to communicate with films local db
@Dao
interface FilmDao {
    @Query("SELECT * FROM cached_films")
    fun getCachedFilms(): Flow<List<Film>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Film>?)

    @Query("DELETE FROM cached_films")
    fun deleteAll()
}