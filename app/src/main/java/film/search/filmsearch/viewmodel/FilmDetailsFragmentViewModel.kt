package film.search.filmsearch.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import film.search.filmsearch.App
import film.search.filmsearch.domain.Interactor
import java.net.URL
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FilmDetailsFragmentViewModel : ViewModel() {
    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
    }

    suspend fun loadWallpaper(urlString: String): Bitmap? {
        return suspendCoroutine {
            val url = URL(urlString)
            val bitmap: Bitmap? = try {
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: Exception) {
                null
            }
            it.resume(bitmap)
        }
    }
}