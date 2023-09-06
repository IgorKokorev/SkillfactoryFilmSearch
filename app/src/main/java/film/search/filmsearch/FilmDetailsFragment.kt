package film.search.filmsearch

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import film.search.filmsearch.MainActivity.Companion.films
import film.search.filmssearch.R
import film.search.filmssearch.databinding.FragmentFilmDetailsBinding

class FilmDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFilmDetailsBinding
    private lateinit var film: Film

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilmDetailsBinding.inflate(layoutInflater)

        val filmId = arguments?.getInt("filmId")
        if (filmId == null) return binding.root

        film = films[filmId]

        binding.detailsToolbar.title = film.title
        binding.detailsPoster.setImageResource(film.poster)
        binding.detailsDescription.text = film.description

        setFavoriteIcon()

        binding.detailsFabFavorites.setOnClickListener {
            film.isFavorite = !film.isFavorite
            setFavoriteIcon()
        }

        binding.detailsFab.setOnClickListener {
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
                        "Check out this film: ${film.title} \n\n ${film.description}"
                    )
                    intent.type = "text/plain"
                    startActivity(Intent.createChooser(intent, "Share To:"))
                }
                .show()
        }

        return binding.root
    }

    private fun setFavoriteIcon() {
        binding.detailsFabFavorites.setImageResource(
            if (film.isFavorite) R.drawable.icon_favorite
            else R.drawable.icon_favorite_border
        )
    }
}