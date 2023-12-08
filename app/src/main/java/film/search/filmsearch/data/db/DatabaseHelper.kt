package film.search.filmsearch.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        // Creating DB if not exists
        db?.execSQL(
            "CREATE TABLE $FILMS_TABLE_NAME (" +
                    "$FILMS_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$FILMS_COLUMN_TITLE TEXT UNIQUE," +
                    "$FILMS_COLUMN_POSTER TEXT," +
                    "$FILMS_COLUMN_DESCRIPTION TEXT," +
                    "$FILMS_COLUMN_RATING REAL);"
        )
    }

    // No migrations yet
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    companion object {
        private const val DATABASE_NAME = "films.db" // DB name
        private const val DATABASE_VERSION = 1 // DB version

        // Constants
        const val FILMS_TABLE_NAME = "films_table"
        const val FILMS_COLUMN_ID = "id"
        const val FILMS_COLUMN_TITLE = "title"
        const val FILMS_COLUMN_POSTER = "poster_path"
        const val FILMS_COLUMN_DESCRIPTION = "overview"
        const val FILMS_COLUMN_RATING = "vote_average"
    }
}