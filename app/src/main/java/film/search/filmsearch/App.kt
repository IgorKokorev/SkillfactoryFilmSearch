package film.search.filmsearch

import android.app.Application
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import film.search.filmsearch.di.AppComponent
import film.search.filmsearch.di.DaggerAppComponent
import film.search.filmsearch.di.modules.DatabaseModule
import film.search.filmsearch.di.modules.DomainModule
import film.search.retrofit.DaggerRemoteComponent

class App : Application() {
    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        // Creating dagger component
        dagger = DaggerAppComponent.builder()
            .remoteProvider(DaggerRemoteComponent.create())
            .databaseModule(DatabaseModule())
            .domainModule(DomainModule(this))
            .build()

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("MainActivity", "Fetching FCM registration token failed", task.exception);
                    return@OnCompleteListener
                }
                Log.i("MainActivity", task.result!!)
            })
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
