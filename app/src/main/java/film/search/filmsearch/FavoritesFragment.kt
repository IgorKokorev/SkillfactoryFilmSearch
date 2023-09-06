package film.search.filmsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import film.search.filmsearch.MainActivity.Companion.favoriteFilms
import film.search.filmsearch.MainActivity.Companion.films
import film.search.filmssearch.databinding.FragmentFavoritesBinding

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
        for (film in films) {
            if (film.isFavorite) favoriteFilms.add(film)
        }

        filmsAdapter = FilmRecyclerAdapter(object : FilmRecyclerAdapter.OnItemClickListener {
            override fun click(film: Film) {
                (requireActivity() as MainActivity).launchDetailsFragment(film)
            }
        })

        filmsAdapter.addItems(favoriteFilms)
        binding.favoritesRecycler.adapter = filmsAdapter
        val decorator = TopSpacingItemDecoration(8)
        binding.favoritesRecycler.addItemDecoration(decorator)
    }

}