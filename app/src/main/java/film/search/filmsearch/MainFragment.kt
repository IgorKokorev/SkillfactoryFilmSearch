package film.search.filmsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        // Fake film db
        val films = mutableListOf(
            Film(
                R.drawable.brave,
                getString(R.string.brave_title),
                getString(R.string.brave_description)
            ),
            Film(
                R.drawable.cars,
                getString(R.string.cars_title),
                getString(R.string.cars_description)
            ),
            Film(
                R.drawable.finding_nemo,
                getString(R.string.finding_nemo_title),
                getString(R.string.finding_nemo_description)
            ),
            Film(
                R.drawable.incredibles,
                getString(R.string.incredibles_title),
                getString(R.string.incredibles_description)
            ),
            Film(
                R.drawable.lightyear,
                getString(R.string.lightyear_title),
                getString(R.string.lightyear_description)
            ),
            Film(
                R.drawable.luca,
                getString(R.string.luca_title),
                getString(R.string.luca_description)
            ),
            Film(
                R.drawable.monsters_inc,
                getString(R.string.monsters_inc_title),
                getString(R.string.monsters_inc_description)
            ),
            Film(
                R.drawable.onward,
                getString(R.string.onward_title),
                getString(R.string.onward_description)
            ),
            Film(
                R.drawable.ratatouille,
                getString(R.string.ratatouille_title),
                getString(R.string.ratatouille_description)
            ),
            Film(
                R.drawable.soul,
                getString(R.string.soul_title),
                getString(R.string.soul_description)
            ),
            Film(
                R.drawable.toy_story,
                getString(R.string.toy_story_title),
                getString(R.string.toy_story_description)
            ),
            Film(
                R.drawable.toy_story_four,
                getString(R.string.toy_story_4_title),
                getString(R.string.toy_story_4_description)
            ),
            Film(
                R.drawable.walle,
                getString(R.string.walle_title),
                getString(R.string.walle_description)
            )
        )

        filmsAdapter.addItems(films)

        binding.mainRecycler.adapter = filmsAdapter

        val decorator = TopSpacingItemDecoration(8)
        binding.mainRecycler.addItemDecoration(decorator)
    }
}