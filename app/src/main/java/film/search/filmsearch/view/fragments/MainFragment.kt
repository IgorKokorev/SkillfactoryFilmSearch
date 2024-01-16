package film.search.filmsearch.view.fragments

//import film.search.filmsearch.view.MainActivity.Companion.allFilms
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import film.search.filmsearch.R
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.databinding.FilmItemBinding
import film.search.filmsearch.databinding.FragmentMainBinding
import film.search.filmsearch.utils.AnimationHelper
import film.search.filmsearch.view.MainActivity
import film.search.filmsearch.view.rvadapters.FilmRecyclerAdapter
import film.search.filmsearch.view.rvadapters.TopSpacingItemDecoration
import film.search.filmsearch.viewmodel.MainFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Main fragment with list of films
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var filmsAdapter: FilmRecyclerAdapter
    private val viewModel: MainFragmentViewModel by activityViewModels()
    private lateinit var scope: CoroutineScope

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
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup all listeners for data from the ViewModel
        setupDataFromViewModel()

        // Setting the whole search view clickable
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }

        // Setting the 'on-the-fly' search logic
        setUpSearch()

        // initializing RecyclerView
        initRecycler()

        // initializing swipe refresh
        initPullToRefresh()

        refreshFragment()

        AnimationHelper.performFragmentCircularRevealAnimation(binding.mainFragmentRoot, requireActivity(), 0)

    }

    // cancel scope to stop coroutines
    override fun onStop() {
        super.onStop()
        scope.cancel()
    }

    private fun setupDataFromViewModel() {

        // reading film list in a coroutine
        scope = CoroutineScope(Dispatchers.IO).also { scope ->
            scope.launch {
                viewModel.filmsList.collect {
                    withContext(Dispatchers.Main) {
                        filmsDataBase = it
                    }
                }
            }
        }

        // Checking in cycle if we need to show progress bar
        scope.launch {
            for (element in viewModel.showProgressBar) {
                launch(Dispatchers.Main) {
                    binding.progressBar.isVisible = element
                }
            }
        }

        // Listening for "API error" event
        viewModel.apiErrorEvent.observe(viewLifecycleOwner) {
            Toast.makeText(binding.root.context, it + ": " + getString(R.string.api_error_message), Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpSearch() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {

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
        binding.mainRecycler.adapter = filmsAdapter

        // adding decorator with spaces between items
        val decorator = TopSpacingItemDecoration(8)
        binding.mainRecycler.addItemDecoration(decorator)

        // Hide/show search view depending on recycler view scroll direction
        // Download new data when we're at the end of the list
        val scrollListener = object : OnScrollListener() {
            private val layoutManager: LinearLayoutManager = binding.mainRecycler.layoutManager as LinearLayoutManager
            var isLoading = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    binding.searchView.visibility = View.GONE
                } else if (dy > 0) {
                    binding.searchView.visibility = View.VISIBLE
                }

                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItems = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading) {
                    if (visibleItemCount + firstVisibleItems >= totalItemCount - 3) {
                        isLoading = true
                        viewModel.addNextPage()
                        isLoading = false
                    }
                }
            }
        }
        binding.mainRecycler.addOnScrollListener(scrollListener)
    }

    private fun initPullToRefresh() {
        binding.pullToRefresh.setOnRefreshListener {

            refreshFragment()

            binding.pullToRefresh.isRefreshing = false
        }
    }

    private fun refreshFragment() {
        viewModel.loadFirstPage()
        setupDataFromViewModel()
        filmsAdapter.addItems(filmsDataBase)
    }
}
