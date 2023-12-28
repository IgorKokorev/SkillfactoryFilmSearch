package film.search.filmsearch.view.fragments

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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
import film.search.filmsearch.App
import film.search.filmsearch.R
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.data.tmbd.ApiConstants
import film.search.filmsearch.databinding.FragmentFilmDetailsBinding
import film.search.filmsearch.viewmodel.FilmDetailsFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// Fragment with film details
class FilmDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFilmDetailsBinding
    private lateinit var film: Film
    private val scope = CoroutineScope(Dispatchers.IO)
    private val viewModel: FilmDetailsFragmentViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilmDetailsBinding.inflate(layoutInflater)

        // getting film as argument. Method depends on OS version
        film = (
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arguments?.getParcelable(App.instance.FILM, Film::class.java)
                } else {
                    arguments?.getParcelable(App.instance.FILM) as Film?
                }
                ) ?: return binding.root

        // setting views data
        binding.detailsToolbar.title = film.title
        Glide.with(this)
            .load(ApiConstants.IMAGES_URL + "w780" + film.poster)
            .centerCrop()
            .into(binding.poster)
//        binding.poster.setImageResource(film.poster)
        binding.description.text = film.description

        setFavoriteFAB()
        setShareFAB()
        setDownloadFAB()


        return binding.root
    }

    private fun setDownloadFAB() {
        binding.detailsFabDownloadWp.setOnClickListener {
            performAsyncLoadOfPoster()
        }
    }

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

    private fun setFavoriteFAB() {

        // setting 'add to favorites' fab click listener
        binding.favoritesFab.setOnClickListener {
            film.isFavorite = !film.isFavorite
            setFavoriteFAB()
        }

        // changing 'add to favorites' fab icon depending on status
        binding.favoritesFab.setImageResource(
            if (film.isFavorite) R.drawable.icon_favorite
            else R.drawable.icon_favorite_border
        )

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

    private fun performAsyncLoadOfPoster() {
        // Handle permission for older system versions
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
                Toast.makeText(binding.root.context, R.string.api_error_message, Toast.LENGTH_SHORT).show()
            } else { // bitmap was loaded correctly
                saveImageToGallery(result) // Save image to gallery once loaded
                Snackbar.make(
                    binding.root,
                    R.string.downloaded_to_gallery,
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.open_in_gallery) {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.type = "image/*"
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    .show()
            }

            binding.progressBar.isVisible = false // ProgressBar OFF
        }
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        // For newer Android versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.TITLE, film.title.handleSingleQuote())
                put(MediaStore.Images.Media.DISPLAY_NAME, film.title.handleSingleQuote())
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FilmsSearchApp")
            }
            val contentResolver = requireActivity().contentResolver
            val uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            val outputStream = contentResolver.openOutputStream(uri!!)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream!!)
            outputStream?.close()
        } else {
            // For older system versions
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.insertImage(
                requireActivity().contentResolver,
                bitmap,
                film.title.handleSingleQuote(),
                film.description.handleSingleQuote()
            )
        }
    }

    // Remove quotes from string
    private fun String.handleSingleQuote(): String {
        return this.replace("'", "")
    }
}
