package film.search.filmsearch.data.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import film.search.filmsearch.data.entity.WatchLaterFilm
import io.reactivex.rxjava3.core.Observable

// Room's DAO interface to communicate with films local db
@Dao
interface WatchLaterFilmDao {
    @Query("SELECT * FROM watch_later_films")
    fun getWatchLaterFilms(): Observable<List<WatchLaterFilm>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: WatchLaterFilm)

    @Delete
    fun delete(film: WatchLaterFilm)

    @Query("DELETE FROM watch_later_films WHERE tmdb_id = :tmdbId")
    fun deleteByTmdbId(tmdbId: Int)

    @Query("SELECT EXISTS (SELECT * FROM watch_later_films WHERE tmdb_id = :tmdbId)")
    fun existsByTmdbId(tmdbId: Int): Observable<Boolean>

}