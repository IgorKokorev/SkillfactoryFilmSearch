package film.search.filmsearch

import android.os.Bundle
import android.transition.AutoTransition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import film.search.filmsearch.MainActivity.Companion.allFilms
import film.search.filmsearch.MainActivity.Companion.filmDetailsFragment
import film.search.filmssearch.databinding.FilmItemBinding
import film.search.filmssearch.databinding.FragmentMainBinding

// Main fragment with list of films
class MainFragment : Fragment() {
    private lateinit var mainBinding: FragmentMainBinding
    private lateinit var filmsAdapter: FilmRecyclerAdapter

    init {
/*        sharedElementEnterTransition = AutoTransition().setDuration(1000L)
        sharedElementReturnTransition = AutoTransition().setDuration(1000L)*/
/*        filmDetailsFragment.sharedElementEnterTransition = AutoTransition().setDuration(1000L)
        filmDetailsFragment.sharedElementReturnTransition = AutoTransition().setDuration(1000L)*/

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // inflating the fragment
        mainBinding = FragmentMainBinding.inflate(layoutInflater)
        return mainBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedElementEnterTransition = AutoTransition().setDuration(1000L)
        sharedElementReturnTransition = AutoTransition().setDuration(1000L)

        // Setting the whole search view clickable
        mainBinding.searchView.setOnClickListener {
            mainBinding.searchView.isIconified = false
        }

        // Setting the 'on-the-fly' search logic
        mainBinding.searchView.setOnQueryTextListener(object : OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    filmsAdapter.addItems(allFilms)
                    return true
                }

                val result = allFilms.filter {
                    it.title.lowercase().contains(newText.lowercase())
                }

                filmsAdapter.addItems(result as MutableList<Film>)
                return true
            }

        })

        // Hide/show search view depending on recycler view scroll direction
        mainBinding.mainRecycler.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    mainBinding.searchView.visibility = View.GONE
                } else if (dy > 0) {
                    mainBinding.searchView.visibility = View.VISIBLE
                }
            }
        })

        // initializing RecyclerView
        initRecycler()

    }
    // Initializing Recycler view with films
    private fun initRecycler() {
        // on item click listener
        filmsAdapter = FilmRecyclerAdapter(object : FilmRecyclerAdapter.OnItemClickListener {
            // If a film is clicked a new fragment with film details is launched
            override fun click(film: Film, position: Int, binding: FilmItemBinding) {
                (requireActivity() as MainActivity).launchDetailsFragment(film, position, binding)
            }
        })
        // adding all the films to recycler view adapter
        filmsAdapter.addItems(allFilms)
        mainBinding.mainRecycler.adapter = filmsAdapter
        // adding decorator with spaces between items
        val decorator = TopSpacingItemDecoration(8)
        mainBinding.mainRecycler.addItemDecoration(decorator)
    }
}
