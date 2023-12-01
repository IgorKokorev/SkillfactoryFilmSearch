package film.search.filmsearch.view.fragments

//import film.search.filmsearch.view.MainActivity.Companion.allFilms
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import film.search.filmsearch.domain.Film
import film.search.filmsearch.utils.AnimationHelper
import film.search.filmsearch.view.MainActivity
import film.search.filmsearch.view.rvadapters.FilmRecyclerAdapter
import film.search.filmsearch.view.rvadapters.TopSpacingItemDecoration
import film.search.filmsearch.viewmodel.MainFragmentViewModel
import film.search.filmssearch.databinding.FilmItemBinding
import film.search.filmssearch.databinding.FragmentMainBinding

// Main fragment with list of films
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var filmsAdapter: FilmRecyclerAdapter
    private var page = 1
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
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.filmsListLiveData.observe(viewLifecycleOwner, Observer<List<Film>> {
            filmsDataBase = it
        })

        // Setting the whole search view clickable
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }

        // Setting the 'on-the-fly' search logic
        setUpSearch()

        // initializing RecyclerView
        initRecycler()

        AnimationHelper.performFragmentCircularRevealAnimation(binding.mainFragmentRoot, requireActivity(), 0)

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
            //Чистим адаптер(items нужно будет сделать паблик или создать для этого публичный метод)
            filmsAdapter.films.clear()
            //Делаем новый запрос фильмов на сервер
            viewModel.addNextPage()
            //Убираем крутящееся колечко
            binding.pullToRefresh.isRefreshing = false
        }
    }
}
