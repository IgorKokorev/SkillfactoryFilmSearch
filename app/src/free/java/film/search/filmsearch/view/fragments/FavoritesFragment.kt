package film.search.filmsearch.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import film.search.filmsearch.Constants
import film.search.filmsearch.R
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.databinding.FilmItemBinding
import film.search.filmsearch.databinding.FragmentFavoritesBinding
import film.search.filmsearch.utils.AnimationHelper
import film.search.filmsearch.utils.AutoDisposable
import film.search.filmsearch.utils.addTo
import film.search.filmsearch.view.MainActivity
import film.search.filmsearch.view.rvadapters.FilmRecyclerAdapter
import film.search.filmsearch.view.rvadapters.TopSpacingItemDecoration
import film.search.filmsearch.viewmodel.FavoritesFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

// List of favorites films fragment. Mostly the same as main fragment
class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var filmsAdapter: FilmRecyclerAdapter
    private val viewModel: FavoritesFragmentViewModel by viewModels<FavoritesFragmentViewModel>()
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
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val time = viewModel.interactor.getFistFreeAccessTime()
        if (time == 0L) {
            viewModel.interactor.saveFistFreeAccessTime()
            showAlertDialogFreeVersionLimit {
                Toast.makeText(
                    binding.root.context,
                    getString(R.string.don_t_forget_to_subscribe),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (time + Constants.FREE_VERSION_USE_TIME < System.currentTimeMillis()) {
            showAlertDialogFreeVersionLimit {
                activity?.supportFragmentManager?.popBackStack()
            }
        }
        viewModel.filmsList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list -> filmsDataBase = list }
            .addTo(autoDisposable)

        initRecycler()
        AnimationHelper.performFragmentCircularRevealAnimation(binding.root, requireActivity(), 1)
    }

    private fun showAlertDialogFreeVersionLimit(action: () -> Unit) {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle(getString(R.string.first_free_access_title))
            .setMessage(getString(R.string.free_access_message))
            .setNeutralButton(
                getString(R.string.ok)
            ) { dialog, which ->
                action.invoke()
            }
            .show()
    }

    // initializing 'favorites' recycler view
    private fun initRecycler() {

        filmsAdapter = FilmRecyclerAdapter(object : FilmRecyclerAdapter.OnItemClickListener {
            override fun click(film: Film, position: Int, binding: FilmItemBinding) {
                (requireActivity() as MainActivity).launchDetailsFragment(film)
            }
        })

        filmsAdapter.addItems(filmsDataBase.filter { film -> film.isFavorite })
        binding.favoritesRecycler.adapter = filmsAdapter
        val decorator = TopSpacingItemDecoration(8)
        binding.favoritesRecycler.addItemDecoration(decorator)
    }
}
