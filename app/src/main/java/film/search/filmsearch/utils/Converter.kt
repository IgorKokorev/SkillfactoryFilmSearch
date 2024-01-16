package film.search.filmsearch.utils

import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.data.tmbd.TmdbFilm

object Converter {
    fun convertApiListToFilmList(list: List<TmdbFilm>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            result.add(convertApiToFilm(it))
        }
        return result
    }

    fun convertApiToFilm(apiData: TmdbFilm): Film {
        return Film(
            title = apiData.title,
            poster = apiData.posterPath,
            description = apiData.overview,
            rating = apiData.voteAverage / 10f,
            isFavorite = false
        )
    }
}