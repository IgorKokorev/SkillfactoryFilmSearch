package film.search.filmsearch.data

import film.search.filmsearch.data.DAO.FilmDao
import film.search.filmsearch.data.entity.Film
import java.util.concurrent.Executors

// Responsible for interchanging data between ViewModels and DB
class MainRepository(private val filmDao: FilmDao) {

    fun putToDb(films: List<Film>) {
        Executors.newSingleThreadExecutor().execute {
            filmDao.insertAll(films)
        }
    }

    fun getAllFromDB(): List<Film> {
        return filmDao.getCachedFilms()
    }

/*    // Put a new film to DB
    fun putToDb(film: Film) {
        val cv = ContentValues()
        cv.apply {
            put(DatabaseHelper.FILMS_COLUMN_TITLE, film.title)
            put(DatabaseHelper.FILMS_COLUMN_POSTER, film.poster)
            put(DatabaseHelper.FILMS_COLUMN_DESCRIPTION, film.description)
            put(DatabaseHelper.FILMS_COLUMN_RATING, film.rating)
        }
        sqlDb.insert(DatabaseHelper.FILMS_TABLE_NAME, null, cv)
    }

    // Get all films from DB
    fun getAllFromDB(): List<Film> {
        cursor = sqlDb.rawQuery("SELECT * FROM ${DatabaseHelper.FILMS_TABLE_NAME}", null)
        val result = mutableListOf<Film>()
        cursor.use { c ->
            if (c.moveToFirst()) {
                do {
                    val title = c.getString(1)
                    val poster = c.getString(2)
                    val description = c.getString(3)
                    val rating = c.getDouble(4)

                    result.add(Film(title, poster, description, rating))
                } while (c.moveToNext())
            }
        }
        return result
    }

    // Update film in DB
    fun updateFilmInDB(film: Film) {
        val cv = ContentValues()
        cv.apply {
            put(DatabaseHelper.FILMS_COLUMN_POSTER, film.poster)
            put(DatabaseHelper.FILMS_COLUMN_DESCRIPTION, film.description)
            put(DatabaseHelper.FILMS_COLUMN_RATING, film.rating)
        }

        sqlDb.update(
            DatabaseHelper.FILMS_TABLE_NAME,
            cv,
            DatabaseHelper.FILMS_COLUMN_TITLE + " = ?",
            arrayOf(film.title)
        )
    }

    // Delete film from DB
    fun deleteFilmFromDB(film: Film) {
        sqlDb.delete(
            DatabaseHelper.FILMS_TABLE_NAME,
            DatabaseHelper.FILMS_COLUMN_TITLE + " = ?",
            arrayOf(film.title)
        )
    }

    // Get all films from DB with rating not less than
    fun getFilmsFromDBWithRating(minRating: Double): List<Film> {
        cursor = sqlDb.rawQuery(
            "SELECT * FROM ${DatabaseHelper.FILMS_TABLE_NAME} " +
                    "WHERE ${DatabaseHelper.FILMS_COLUMN_RATING} >= ?",
            arrayOf(minRating.toString())
        )

        val result = mutableListOf<Film>()
        cursor.use { c ->
            if (c.moveToFirst()) {
                do {
                    val title = c.getString(1)
                    val poster = c.getString(2)
                    val description = c.getString(3)
                    val rating = c.getDouble(4)

                    result.add(Film(title, poster, description, rating))
                } while (c.moveToNext())
            }
        }
        return result
    }

    // Get all films from DB with titles containing a string
    fun getFilmsFromDBByTitle(titlePattern: String): List<Film> {
        cursor = sqlDb.rawQuery(
            "SELECT * FROM ${DatabaseHelper.FILMS_TABLE_NAME} " +
                    "WHERE ${DatabaseHelper.FILMS_COLUMN_TITLE} LIKE ?",
            arrayOf("%$titlePattern%")
        )

        val result = mutableListOf<Film>()
        cursor.use { c ->
            if (c.moveToFirst()) {
                do {
                    val title = c.getString(1)
                    val poster = c.getString(2)
                    val description = c.getString(3)
                    val rating = c.getDouble(4)

                    result.add(Film(title, poster, description, rating))
                } while (c.moveToNext())
            }
        }
        return result
    }

    // Get all films from DB with description containing a string
    fun getFilmsFromDBByDescription(descriptionPattern: String): List<Film> {
        cursor = sqlDb.rawQuery(
            "SELECT * FROM ${DatabaseHelper.FILMS_TABLE_NAME} " +
                    "WHERE ${DatabaseHelper.FILMS_COLUMN_DESCRIPTION} LIKE ?",
            arrayOf("%$descriptionPattern%")
        )

        val result = mutableListOf<Film>()
        cursor.use { c ->
            if (c.moveToFirst()) {
                do {
                    val title = c.getString(1)
                    val poster = c.getString(2)
                    val description = c.getString(3)
                    val rating = c.getDouble(4)

                    result.add(Film(title, poster, description, rating))
                } while (c.moveToNext())
            }
        }
        return result
    }*/
}