package film.search.filmsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import film.search.filmssearch.R
import film.search.filmssearch.databinding.FragmentFilmDetailsBinding

class FilmDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFilmDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilmDetailsBinding.inflate(layoutInflater)

        val film = arguments?.get("film") as Film

        binding.detailsToolbar.title = film.title
        binding.detailsPoster.setImageResource(film.poster)
        binding.detailsDescription.text = film.description

        binding.detailsFab.setOnClickListener {
            Snackbar.make(
                binding.root,
                getString(R.string.send_to_friend, film.title),
                Snackbar.LENGTH_SHORT
            )
                .setAction(getString(R.string.share), {})
                .show()
        }

        return binding.root
    }
}