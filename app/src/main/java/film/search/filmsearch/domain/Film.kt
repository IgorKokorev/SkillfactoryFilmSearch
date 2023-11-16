package film.search.filmsearch.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Data class with film's data
@Parcelize
data class Film(
    val poster: String,
    val title: String,
    val description: String,
    val rating: Double,
    var isFavorite: Boolean = false
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Film

        if (poster != other.poster) return false
        if (title != other.title) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = poster.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }
}
