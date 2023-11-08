package film.search.filmsearch.data

import android.content.Context
import film.search.filmsearch.domain.Film
import film.search.filmssearch.R

// Main repository contains fake film db
class MainRepository(val context: Context) {
    val allFilms = mutableListOf(
        Film(
            R.drawable.brave,
            context.getString(R.string.brave_title),
            context.getString(R.string.brave_description),
            49
        ),
        Film(
            R.drawable.cars,
            context.getString(R.string.cars_title),
            context.getString(R.string.cars_description),
            99
        ),
        Film(
            R.drawable.finding_nemo,
            context.getString(R.string.finding_nemo_title),
            context.getString(R.string.finding_nemo_description),
            56
        ),
        Film(
            R.drawable.incredibles,
            context.getString(R.string.incredibles_title),
            context.getString(R.string.incredibles_description),
            83
        ),
        Film(
            R.drawable.lightyear,
            context.getString(R.string.lightyear_title),
            context.getString(R.string.lightyear_description),
            23
        ),
        Film(
            R.drawable.luca,
            context.getString(R.string.luca_title),
            context.getString(R.string.luca_description),
            38
        ),
        Film(
            R.drawable.monsters_inc,
            context.getString(R.string.monsters_inc_title),
            context.getString(R.string.monsters_inc_description),
            100
        ),
        Film(
            R.drawable.onward,
            context.getString(R.string.onward_title),
            context.getString(R.string.onward_description),
            72
        ),
        Film(
            R.drawable.ratatouille,
            context.getString(R.string.ratatouille_title),
            context.getString(R.string.ratatouille_description),
            0
        ),
        Film(
            R.drawable.soul,
            context.getString(R.string.soul_title),
            context.getString(R.string.soul_description),
            88
        ),
        Film(
            R.drawable.toy_story,
            context.getString(R.string.toy_story_title),
            context.getString(R.string.toy_story_description),
            15
        ),
        Film(
            R.drawable.toy_story_four,
            context.getString(R.string.toy_story_4_title),
            context.getString(R.string.toy_story_4_description),
            16
        ),
        Film(
            R.drawable.walle,
            context.getString(R.string.walle_title),
            context.getString(R.string.walle_description),
            93
        )
    )
}
