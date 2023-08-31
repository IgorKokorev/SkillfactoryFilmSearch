package film.search.filmsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import film.search.filmsearch.MainActivity.Companion.films
import film.search.filmssearch.R
import film.search.filmssearch.databinding.FragmentMainBinding

// Main fragment with list of films
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var filmsAdapter: FilmRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        initRecycler()
        return binding.root
    }

    // Initializing Recycler view with films
    private fun initRecycler() {
        filmsAdapter = FilmRecyclerAdapter(object : FilmRecyclerAdapter.OnItemClickListener {
            override fun click(film: Film) {
                (requireActivity() as MainActivity).launchDetailsFragment(film)
            }
        })
        filmsAdapter.addItems(films)
        binding.mainRecycler.adapter = filmsAdapter
        val decorator = TopSpacingItemDecoration(8)
        binding.mainRecycler.addItemDecoration(decorator)
    }
}