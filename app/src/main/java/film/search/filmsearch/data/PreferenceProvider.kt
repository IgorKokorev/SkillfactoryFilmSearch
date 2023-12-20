package film.search.filmsearch.data

import android.content.Context
import android.content.SharedPreferences

// Working with SharedPreferences of the app
class PreferenceProvider(context: Context) {
    // Application context
    private val appContext = context.applicationContext
    // SharedPreferences
    private val preference: SharedPreferences = appContext.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)

    // Initializing SharedPreferences for the app if launched for the first time
    init {
        if(preference.getBoolean(KEY_FIRST_LAUNCH, false)) {
            preference.edit().putString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY).apply()
            preference.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
            saveLastAPIRequestTime()
        }
    }

    // Default films category
    fun saveDefaultCategory(category: String) {
        preference.edit().putString(KEY_DEFAULT_CATEGORY, category).apply()
    }

    fun getDefaultCategory(): String {
        return preference.getString(KEY_DEFAULT_CATEGORY, DEFAULT_CATEGORY) ?: DEFAULT_CATEGORY
    }

    // Last time in millis when remote API was accessed
    fun saveLastAPIRequestTime() {
        val time = System.currentTimeMillis()
        preference.edit().putLong(KEY_LAST_DOWNLOAD_TIME, time).apply()
    }

    fun getLastAPIRequestTime(): Long {
        return preference.getLong(KEY_LAST_DOWNLOAD_TIME, 0L)
    }

    // What category was saved to local db
    fun saveCategoryInDB(category: String) {
        preference.edit().putString(KEY_LOCAL_DB_CATEGORY, category).apply()
    }

    fun getCategoryInDB(): String {
        return preference.getString(KEY_LOCAL_DB_CATEGORY, DEFAULT_DB_CATEGORY) ?: DEFAULT_DB_CATEGORY
    }

    // Constants
    companion object {
        private const val SETTINGS = "settings"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_DEFAULT_CATEGORY = "default_category"
        private const val DEFAULT_CATEGORY = "popular"
        private const val KEY_LAST_DOWNLOAD_TIME = "last_time"
        private const val KEY_LOCAL_DB_CATEGORY = "db_category"
        private const val DEFAULT_DB_CATEGORY = "none"
    }
}