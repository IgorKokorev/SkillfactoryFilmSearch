package film.search.filmsearch.utils

import film.search.filmsearch.data.tmbd.TmdbFilm
import film.search.filmsearch.data.entity.Film

object Converter {
    fun convertApiListToFilmList(list: List<TmdbFilm>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(
                Film(
                    title = it.title,
                    poster = it.posterPath,
                    description = it.overview,
                    rating = it.voteAverage / 10f,
                    isFavorite = false
            )
            )
        }
        return result
    }
}