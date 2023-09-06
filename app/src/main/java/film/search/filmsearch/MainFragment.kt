package film.search.filmsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import film.search.filmsearch.MainActivity.Companion.allFilms
import film.search.filmssearch.databinding.FragmentMainBinding

// Main fragment with list of films
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var filmsAdapter: FilmRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflating the fragment
        binding = FragmentMainBinding.inflate(layoutInflater)

        // initializing RecyclerView
        initRecycler()

        // Setting the whole search view clickable
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }

        // Setting the 'on-the-fly' search logic
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
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
        binding.mainRecycler.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    binding.searchView.visibility = View.GONE
                } else if (dy > 0) {
                    binding.searchView.visibility = View.VISIBLE
                }
            }
        })

        return binding.root
    }

    // Initializing Recycler view with films
    private fun initRecycler() {
        // on item click listener
        filmsAdapter = FilmRecyclerAdapter(object : FilmRecyclerAdapter.OnItemClickListener {
            // If a film is clicked a new fragment with film details is launched
            override fun click(film: Film) {
                (requireActivity() as MainActivity).launchDetailsFragment(film)
            }
        })
        // adding all the films to recycler view adapter
        filmsAdapter.addItems(allFilms)
        binding.mainRecycler.adapter = filmsAdapter
        // adding decorator with spaces between items
        val decorator = TopSpacingItemDecoration(8)
        binding.mainRecycler.addItemDecoration(decorator)
    }
}