package film.search.filmsearch.view.fragments

//import film.search.filmsearch.view.MainActivity.Companion.allFilms
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.databinding.FilmItemBinding
import film.search.filmsearch.databinding.FragmentMainBinding
import film.search.filmsearch.utils.AnimationHelper
import film.search.filmsearch.utils.AutoDisposable
import film.search.filmsearch.utils.addTo
import film.search.filmsearch.view.MainActivity
import film.search.filmsearch.view.rvadapters.FilmRecyclerAdapter
import film.search.filmsearch.view.rvadapters.TopSpacingItemDecoration
import film.search.filmsearch.viewmodel.MainFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

// Main fragment with list of films
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var filmsAdapter: FilmRecyclerAdapter
    private val viewModel: MainFragmentViewModel by viewModels<MainFragmentViewModel>()
    private val autoDisposable = AutoDisposable()
    private var filmsDataBase = listOf<Film>()
        set(value) {
            if (field == value) return
            field = value
            filmsAdapter.addItems(field)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoDisposable.bindTo(lifecycle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup all data from the ViewModel
        setupDataFromViewModel()
        // Setting the whole search view clickable
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }
        // Setting the 'on-the-fly' search logic
        setUpSearch()
        // initializing RecyclerView
        initRecycler()
        // initializing swipe refresh - we don't need it anymore?
        initPullToRefresh()
        refreshFragment()
        AnimationHelper.performFragmentCircularRevealAnimation(binding.mainFragmentRoot, requireActivity(), 0)
    }

    private fun setupDataFromViewModel() {

        viewModel.filmsList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list -> filmsDataBase = list}
            .addTo(autoDisposable)

        viewModel.showProgressBar
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.progressBar.isVisible = it
            }
            .addTo(autoDisposable)
    }

    // Search films in API logic. Search request is sent only after 1 seconds of no input
    private fun setUpSearch() {
        Observable.create<String> {
            binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = true

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrBlank()) {
                        viewModel.loadFirstPage(true)
                        return true
                    }
                    it.onNext(newText)
                    return true
                }
            })
        }
            .debounce(1, TimeUnit.SECONDS)
            .subscribe {
                viewModel.loadSearchResults(it)
            }

    }

    // Initializing Recycler view with films
    private fun initRecycler() {
        // on item click listener
        filmsAdapter = FilmRecyclerAdapter(object : FilmRecyclerAdapter.OnItemClickListener {
            // If a film is clicked a new fragment with film details is launched
            override fun click(film: Film, position: Int, binding: FilmItemBinding) {
                (requireActivity() as MainActivity).launchDetailsFragment(film)
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
                // Making search visible/invisible depending on scroll direction
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    binding.searchView.visibility = View.GONE
                } else if (dy > 0) {
                    binding.searchView.visibility = View.VISIBLE
                }

                // Loading additional data from API when reaching list bottom
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItems = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading) {
                    if (visibleItemCount + firstVisibleItems >= totalItemCount - 3) {
                        isLoading = true
                        if (binding.searchView.query.isNullOrBlank()) {
                            viewModel.addNextPage()
                        } else {
                            viewModel.addSearchResultsPage(binding.searchView.query.toString())
                        }
                        isLoading = false
                    }
                }
            }
        }
        binding.mainRecycler.addOnScrollListener(scrollListener)
    }

    private fun initPullToRefresh() {
        binding.pullToRefresh.setOnRefreshListener {
            binding.pullToRefresh.isRefreshing = false
        }
    }

    private fun refreshFragment() {
        viewModel.loadFirstPage(false)
    }
}
