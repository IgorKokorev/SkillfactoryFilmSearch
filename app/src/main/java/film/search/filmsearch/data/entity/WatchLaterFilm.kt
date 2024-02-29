package film.search.filmsearch.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// Data class with film's data. For Room: table with cached films
@Parcelize
@Entity(tableName = "watch_later_films", indices = [Index(value = ["tmdb_id"], unique = true)])
data class WatchLaterFilm(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "time") var time: Long,
    @ColumnInfo(name = "tmdb_id") val tmdbId: Int,
    @ColumnInfo(name = "poster_path") val poster: String,
    @ColumnInfo(name = "overview") val description: String,
    @ColumnInfo(name = "vote_average") val rating: Double,
    var isFavorite: Boolean = false
) : Parcelable
