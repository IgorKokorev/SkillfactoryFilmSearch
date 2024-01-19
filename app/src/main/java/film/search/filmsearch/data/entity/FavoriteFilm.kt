package film.search.filmsearch.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// Data class with film's data. For Room: table with cached films
@Parcelize
@Entity(tableName = "favorite_films", indices = [Index(value = ["tmdb_id"], unique = true)])
data class FavoriteFilm(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "poster_path") val poster: String,
    @ColumnInfo(name = "overview") val description: String,
    @ColumnInfo(name = "vote_average") val rating: Double,
    @ColumnInfo(name = "tmdb_id") val tmdbId: Int,
) : Parcelable
