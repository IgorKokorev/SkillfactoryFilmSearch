package film.search.filmsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import film.search.filmsearch.MainActivity.Companion.favoriteFilms
import film.search.filmsearch.MainActivity.Companion.allFilms
import film.search.filmssearch.databinding.FilmItemBinding
import film.search.filmssearch.databinding.FragmentFavoritesBinding

// List of favorites films fragment. Mostly the same as main fragment
class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var filmsAdapter: FilmRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        initRecycler()
        return binding.root
    }

    private fun initRecycler() {
        favoriteFilms.clear()
        for (film in allFilms) {
            if (film.isFavorite) favoriteFilms.add(film)
        }

        filmsAdapter = FilmRecyclerAdapter(object : FilmRecyclerAdapter.OnItemClickListener {
            override fun click(film: Film, position: Int, binding: FilmItemBinding) {
                (requireActivity() as MainActivity).launchDetailsFragment(film, position, binding)
            }
        })

        filmsAdapter.addItems(favoriteFilms)
        binding.favoritesRecycler.adapter = filmsAdapter
        val decorator = TopSpacingItemDecoration(8)
        binding.favoritesRecycler.addItemDecoration(decorator)
    }

}
