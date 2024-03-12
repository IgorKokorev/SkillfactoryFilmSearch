package film.search.filmsearch.view.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import film.search.filmsearch.Constants
import film.search.filmsearch.R
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.databinding.FragmentFilmDetailsBinding
import film.search.filmsearch.utils.AlarmService
import film.search.filmsearch.utils.MediaStoreMediator.saveBitmapToGallery
import film.search.filmsearch.viewmodel.FilmDetailsFragmentViewModel
import film.search.retrofit.entity.ApiConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// Fragment with film details
class FilmDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFilmDetailsBinding
    private lateinit var film: Film
    private lateinit var alarmService: AlarmService
    private val scope = CoroutineScope(Dispatchers.IO)
    private val viewModel: FilmDetailsFragmentViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilmDetailsBinding.inflate(layoutInflater)

        alarmService = AlarmService(binding.root.context)

        // getting film as argument. Method depends on OS version
        film = (
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arguments?.getParcelable(Constants.FILM, Film::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    arguments?.getParcelable(Constants.FILM) as Film?
                }
                ) ?: return binding.root

        // setting views data
        binding.detailsToolbar.title = film.title
        Glide.with(this)
            .load(ApiConstants.IMAGES_URL + "w780" + film.poster)
            .centerCrop()
            .into(binding.poster)
        binding.description.text = film.description

        setFavoriteFAB()
        setShareFAB()
        setDownloadFAB()
        setWatchLaterFAB()

        return binding.root
    }

    // set 'watch later' fab. User is to set time when to remind to check the film
    private fun setWatchLaterFAB() {
        binding.detailsFabWatchLater.setOnClickListener {
            alarmService.setFilmNotificationAlarm(film)
        }
    }

    // set 'download poster' fab
    private fun setDownloadFAB() {
        binding.detailsFabDownloadWp.setOnClickListener {
            performAsyncLoadOfPoster()
        }
    }

    // set 'share' fab. Sends film info to a contact
    private fun setShareFAB() {
        // setting share fab click listener
        binding.shareFab.setOnClickListener {
            Snackbar.make(
                binding.root,
                getString(R.string.send_to_friend, film.title),
                Snackbar.LENGTH_SHORT
            )
                .setAction(getString(R.string.share)) {
                    val intent = Intent()
                    intent.action = Intent.ACTION_SEND
                    intent.putExtra(
                        Intent.EXTRA_TEXT,
                        getString(R.string.send_film_text, film.title, film.description)
                    )
                    intent.type = "text/plain"
                    startActivity(Intent.createChooser(intent, getString(R.string.send_film_title)))
                }
                .show()
        }
    }

    // set 'favorite' fab. Adds film to the favorites db
    private fun setFavoriteFAB() {
        // setting 'add to favorites' fab click listener
        binding.favoritesFab.setOnClickListener {
            film.isFavorite = !film.isFavorite
            if (film.isFavorite) {
                viewModel.interactor.saveFilmToFavorites(film)
            }
            else viewModel.interactor.deleteFilmFromFavorites(film)
            setFavoriteIcon()
        }
        setFavoriteIcon()
    }

    // set 'add to favorites' fab icon depending on status
    private fun setFavoriteIcon() {
        binding.favoritesFab.setImageResource(
            if (film.isFavorite) R.drawable.icon_favorite
            else R.drawable.icon_favorite_border
        )
    }

    private fun performAsyncLoadOfPoster() {
        // Handle write external storage permission for older system versions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && !checkWriteExternalStoragePermission()) {
            requestWriteExternalStoragePermission()
            return
        }

        // Create parent scope as we'll touch UI
        MainScope().launch {
            binding.progressBar.isVisible = true  // ProgressBar ON
            val job = scope.async {  // Create async job as we need Bitmap as result
                viewModel.loadWallpaper(ApiConstants.IMAGES_URL + "original" + film.poster)
            }
            val result = job.await()
            // If loading failed
            if (result == null) {
                Toast.makeText(binding.root.context, R.string.api_error_message, Toast.LENGTH_SHORT)
                    .show()
            } else { // bitmap was loaded correctly
                // Save image to gallery once loaded
                val uri = saveBitmapToGallery(
                    result,
                    requireContext().applicationContext,
                    "FilmSearchApp",
                    film.title
                )

                // Show snackbar if image is saved successfully
                if (uri != null) {
                    Snackbar.make(
                        binding.root,
                        R.string.downloaded_to_gallery,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.open_in_gallery) {
                            // Open saved image
                            val intent = Intent()
                            intent.action = Intent.ACTION_VIEW
                            intent.setDataAndType(uri, "image/*")
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()
                } else {
                    Toast.makeText(requireContext(), R.string.error_save_message, Toast.LENGTH_SHORT).show()
                }
            }

            binding.progressBar.isVisible = false // ProgressBar OFF
        }
    }

    private fun checkWriteExternalStoragePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestWriteExternalStoragePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1
        )
    }

}
