package film.search.filmsearch.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import film.search.filmsearch.view.rvadapters.FilmRecyclerAdapter
import film.search.filmsearch.view.MainActivity
//import film.search.filmsearch.view.MainActivity.Companion.allFilms
import film.search.filmsearch.view.rvadapters.TopSpacingItemDecoration
import film.search.filmsearch.domain.Film
import film.search.filmsearch.utils.AnimationHelper
import film.search.filmsearch.viewmodel.MainFragmentViewModel
import film.search.filmssearch.databinding.FilmItemBinding
import film.search.filmssearch.databinding.FragmentMainBinding

// Main fragment with list of films
class MainFragment : Fragment() {
    private lateinit var mainBinding: FragmentMainBinding
    private lateinit var filmsAdapter: FilmRecyclerAdapter
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(MainFragmentViewModel::class.java)
    }
    private var filmsDataBase = listOf<Film>()
        set(value) {
            if (field == value) return
            field = value
            filmsAdapter.addItems(field)
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

        viewModel.filmsListLiveData.observe(viewLifecycleOwner, Observer<List<Film>> {
            filmsDataBase = it
        })

        // Setting the whole search view clickable
        mainBinding.searchView.setOnClickListener {
            mainBinding.searchView.isIconified = false
        }

        // Setting the 'on-the-fly' search logic
        setUpSearch()

        // initializing RecyclerView
        initRecycler()

        AnimationHelper.performFragmentCircularRevealAnimation(mainBinding.mainFragmentRoot, requireActivity(), 0)

    }

    private fun setUpSearch() {
        mainBinding.searchView.setOnQueryTextListener(object : OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    filmsAdapter.addItems(filmsDataBase)
                    return true
                }

                val result = filmsDataBase.filter {
                    it.title.lowercase().contains(newText.lowercase())
                }

                filmsAdapter.addItems(result)
                return true
            }

        })
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
        filmsAdapter.addItems(filmsDataBase)
        mainBinding.mainRecycler.adapter = filmsAdapter

        // adding decorator with spaces between items
        val decorator = TopSpacingItemDecoration(8)
        mainBinding.mainRecycler.addItemDecoration(decorator)

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
    }
}
