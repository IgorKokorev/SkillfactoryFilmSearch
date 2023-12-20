package film.search.filmsearch.view.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import film.search.filmsearch.App
import film.search.filmsearch.data.tmbd.ApiConstants
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.R
import film.search.filmsearch.databinding.FragmentFilmDetailsBinding

// Fragment with film details
class FilmDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFilmDetailsBinding
    private lateinit var film: Film

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

        // setting 'favorite' fab
        setFavoriteIcon()

        // setting 'share' fab
        setShareIcon()

        return binding.root
    }

    private fun setShareIcon() {
        // setting share fab click listener
        binding.shareFab.setOnClickListener {
            Snackbar.make(
                binding.root,
                getString(R.string.send_to_friend, film.title),
                Snackbar.LENGTH_SHORT
            )
                .setAction(getString(R.string.share)) {
//                    App.instance.notificationService.sendNotification(
//                        R.drawable.icon_share,
//                        getString(R.string.notification_title),
//                        getString(R.string.notification_text, film.title)
//                    )

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

    private fun setFavoriteIcon() {

        // setting 'add to favorites' fab click listener
        binding.favoritesFab.setOnClickListener {
            film.isFavorite = !film.isFavorite
            setFavoriteIcon()
        }

        // changing 'add to favorites' fab icon depending on status
        binding.favoritesFab.setImageResource(
            if (film.isFavorite) R.drawable.icon_favorite
            else R.drawable.icon_favorite_border
        )

    }
}
