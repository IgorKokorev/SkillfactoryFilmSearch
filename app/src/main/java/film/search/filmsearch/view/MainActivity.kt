package film.search.filmsearch.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import film.search.filmsearch.domain.Film
import film.search.filmsearch.utils.NotificationService
import film.search.filmsearch.view.fragments.CollectionFragment
import film.search.filmsearch.view.fragments.FavoritesFragment
import film.search.filmsearch.view.fragments.FilmDetailsFragment
import film.search.filmsearch.view.fragments.MainFragment
import film.search.filmsearch.view.fragments.WatchLaterFragment
import film.search.filmssearch.R
import film.search.filmssearch.databinding.ActivityMainBinding
import film.search.filmssearch.databinding.FilmItemBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var backPressed = 0L

    // Contains constants:
    // interval for double tap in ms
    // name of the bundle with film info to transfer to another fragment
    // allFilms - fake film db
    // favoriteFilms - list of favorite films
    companion object {
        const val TIME_INTERVAL = 2000L
        const val FILM = "film"
        const val POSITION = "position"
        const val TRANSITION_NAME = "transition"
        const val POSTER = "poster"
        const val DESCRIPTION = "description"
        const val FRAGMENT_TAG = "tag"
        lateinit var notificationService: NotificationService
//        var allFilms = mutableListOf<Film>()
//        val favoriteFilms = mutableListOf<Film>()
//        val filmDetailsFragment = FilmDetailsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // initializing menu buttons click listeners
        initMenuButtons()

        // initializing fake films db
//        initFilmsDB()

        // initializing notification service
        notificationService = NotificationService(this)

//        filmDetailsFragment.sharedElementEnterTransition = AutoTransition().setDuration(1000L)

        // starting main fragment
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_placeholder, MainFragment())
            .addToBackStack(FRAGMENT_TAG)
            .commit()
    }

    // Check back pressed. If 2 times in less than 2 sec om main screen - exit. If not main screen (fragment) - back.
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                onBackPressedDispatcher.onBackPressed()
                finish()
            } else {
                Toast.makeText(this, getString(R.string.double_tap_toast), Toast.LENGTH_SHORT)
                    .show()
            }

            backPressed = System.currentTimeMillis()
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    // Initialize menu buttons - toasts
    private fun initMenuButtons() {
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Toast.makeText(this, R.string.settings_toast, Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    val tag = "home"
                    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: MainFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_placeholder, fragment, tag)
                        .addToBackStack(FRAGMENT_TAG)
                        .commit()
                    true
                }
                R.id.favorites -> {
                    val tag = "favorites"
                    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: FavoritesFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_placeholder, fragment, tag)
                        .addToBackStack(FRAGMENT_TAG)
                        .commit()
                    true
                }

                R.id.watch_later -> {
                    val tag = "watch later"
                    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: WatchLaterFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_placeholder, fragment, tag)
                        .addToBackStack(FRAGMENT_TAG)
                        .commit()
                    true
                }

                R.id.collections -> {
                    val tag = "collection"
                    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: CollectionFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_placeholder, fragment, tag)
                        .addToBackStack(FRAGMENT_TAG)
                        .commit()
                    true
                }

                else -> false
            }
        }
    }

    // Launch Film Details screen (fragment) and transfer film data to it
    fun launchDetailsFragment(film: Film, position: Int, filmItemBinding: FilmItemBinding) {
        val bundle = Bundle()
        bundle.putParcelable(FILM, film)
        bundle.putInt(POSITION, position)
        val fragment = FilmDetailsFragment()
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(FRAGMENT_TAG)
            .commit()
    }

}
