package film.search.filmsearch.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FilmDetailsFragmentViewModel : ViewModel() {
    suspend fun loadWallpaper(url: String): Bitmap? {
        return suspendCoroutine {
            val url = URL(url)
            val bitmap: Bitmap? = try {
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e: Exception) {
                null
            }
            it.resume(bitmap)
        }
    }
}