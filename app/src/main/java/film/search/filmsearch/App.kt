package film.search.filmsearch

import android.app.Application
import film.search.filmsearch.data.tmbd.ApiConstants
import film.search.filmsearch.data.tmbd.TmdbApi
import film.search.filmsearch.domain.Interactor
import film.search.filmsearch.utils.NotificationService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    lateinit var interactor: Interactor
    lateinit var notificationService: NotificationService
    val TRANSITION_NAME = "transition"
    val TIME_INTERVAL = 2000L
    val FILM = "film"
    val POSITION = "position"
    val POSTER = "poster"
    val DESCRIPTION = "description"
    val FRAGMENT_TAG = "tag"

    override fun onCreate() {
        super.onCreate()
        instance = this

/*        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            })
            .build()*/

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
            .build()

        val retrofitService = retrofit.create(TmdbApi::class.java)
        interactor = Interactor(/*repo, */retrofitService)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
