package film.search.filmsearch.utils

import android.util.Log
import com.google.gson.Gson
import film.search.filmsearch.data.entity.FavoriteFilm
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.data.entity.WatchLaterFilm
import film.search.retrofit.entity.TmdbFilm

object Converter {
    fun convertApiListToFilmList(list: List<TmdbFilm>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            try {
                result.add(convertApiToFilm(it))
            } catch (e: Exception) {}
        }
        return result
    }

    fun convertApiToFilm(apiData: TmdbFilm): Film {
        Log.d("!!! Converter:convertApiToFilm", "TmdbFilm: ${Gson().toJson(apiData)}" )
        return Film(
            title = apiData.title,
            poster = apiData.posterPath,
            description = apiData.overview,
            rating = apiData.voteAverage / 10f,
            tmdbId = apiData.id,
            isFavorite = false
        )
    }

    fun filmToFavorite(film: Film): FavoriteFilm {
        return FavoriteFilm(
            title = film.title,
            poster = film.poster,
            description = film.description,
            rating = film.rating,
            tmdbId = film.tmdbId
        )
    }

    fun favoriteToFilm(favoriteFilm: FavoriteFilm): Film {
        return Film(
            title = favoriteFilm.title,
            poster = favoriteFilm.poster,
            description = favoriteFilm.description,
            rating = favoriteFilm.rating,
            tmdbId = favoriteFilm.tmdbId,
            isFavorite = true
        )
    }

    fun favoriteListToFilmList(list: List<FavoriteFilm>?): List<Film> {
        val result = mutableListOf<Film>()
        list?.forEach {
            try {
                result.add(favoriteToFilm(it))
            } catch (e: Exception) {}
        }
        return result
    }

    fun filmToWatchLater(film: Film, time: Long): WatchLaterFilm {
        return WatchLaterFilm(
            title = film.title,
            time = time,
            tmdbId = film.tmdbId,
            description = film.description,
            poster = film.poster,
            rating = film.rating,
            isFavorite = film.isFavorite
        )
    }

    fun watchLaterToFilm(watchLaterFilm: WatchLaterFilm) : Film {
        return Film(
            title = watchLaterFilm.title,
            poster = watchLaterFilm.poster,
            description = watchLaterFilm.description,
            rating = watchLaterFilm.rating,
            tmdbId = watchLaterFilm.tmdbId,
            isFavorite = watchLaterFilm.isFavorite
        )
    }
}