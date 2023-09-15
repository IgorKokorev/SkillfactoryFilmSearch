package film.search.filmsearch

import android.os.Build
import android.os.Bundle
import android.transition.Scene
import android.transition.Slide
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import film.search.filmsearch.MainActivity.Companion.allFilms
import film.search.filmssearch.R
import film.search.filmssearch.databinding.FragmentMainBinding
import film.search.filmssearch.databinding.MergeHomeScreenContentBinding

// Main fragment with list of films
class MainFragment : Fragment() {
    private lateinit var mainBinding: FragmentMainBinding
    private lateinit var mergeBinding: MergeHomeScreenContentBinding
    private lateinit var filmsAdapter: FilmRecyclerAdapter

    init {

        enterTransition = Slide(Gravity.END).apply {
            duration = 800
        }
        returnTransition = Slide(Gravity.END).apply {
            duration = 800
            mode = Slide.MODE_OUT
        }

        exitTransition = Slide(Gravity.START).apply { duration = 800;mode = Slide.MODE_OUT }
        reenterTransition = Slide(Gravity.START).apply { duration = 800; }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // inflating the fragment
        mainBinding = FragmentMainBinding.inflate(layoutInflater)
        mergeBinding = MergeHomeScreenContentBinding.bind(mainBinding.root)

        return mainBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


/*        val scene = Scene.getSceneForLayout(mainBinding.mainFragmentRoot, R.layout.merge_home_screen_content, requireContext())
        val searchSlide = Slide(Gravity.TOP).addTarget(mergeBinding.searchView)
        val recyclerSlide = Slide(Gravity.BOTTOM).addTarget(mergeBinding.mainRecycler)
        val customTransition = TransitionSet().apply {
            duration = 500
            addTransition(recyclerSlide)
            addTransition(searchSlide)
        }
        TransitionManager.go(scene, customTransition)*/

        // Setting the whole search view clickable
        mergeBinding.searchView.setOnClickListener {
            mergeBinding.searchView.isIconified = false
        }

        // Setting the 'on-the-fly' search logic
        mergeBinding.searchView.setOnQueryTextListener(object : OnQueryTextListener {

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
        mergeBinding.mainRecycler.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    mergeBinding.searchView.visibility = View.GONE
                } else if (dy > 0) {
                    mergeBinding.searchView.visibility = View.VISIBLE
                }
            }
        })

        // initializing RecyclerView
        initRecycler()

//        val scene = Scene.getSceneForLayout(mainBinding.mainFragmentRoot, R.layout.merge_home_screen_content, requireContext())
//        scene.enter()

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
        mergeBinding.mainRecycler.adapter = filmsAdapter
        // adding decorator with spaces between items
        val decorator = TopSpacingItemDecoration(8)
        mergeBinding.mainRecycler.addItemDecoration(decorator)
    }
}
