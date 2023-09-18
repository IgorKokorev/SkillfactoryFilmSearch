package film.search.filmsearch

import android.os.Bundle
import android.text.TextUtils.replace
import android.transition.AutoTransition
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        var allFilms = mutableListOf<Film>()
        val favoriteFilms = mutableListOf<Film>()
        val filmDetailsFragment = FilmDetailsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // initializing menu buttons click listeners
        initMenuButtons()

        // initializing fake films db
        initFilmsDB()

        // initializing notification service
        notificationService = NotificationService(this)

        filmDetailsFragment.sharedElementEnterTransition = AutoTransition().setDuration(1000L)

        // starting main fragment
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_placeholder, MainFragment())
            .addToBackStack(FRAGMENT_TAG)
            .commit()
    }

    private fun initFilmsDB() {
        // Fake film db
        allFilms = mutableListOf(
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
                getString(film.search.filmssearch.R.string.finding_nemo_title),
                getString(film.search.filmssearch.R.string.finding_nemo_description)
            ),
            Film(
                film.search.filmssearch.R.drawable.incredibles,
                getString(film.search.filmssearch.R.string.incredibles_title),
                getString(film.search.filmssearch.R.string.incredibles_description)
            ),
            Film(
                film.search.filmssearch.R.drawable.lightyear,
                getString(film.search.filmssearch.R.string.lightyear_title),
                getString(film.search.filmssearch.R.string.lightyear_description)
            ),
            Film(
                film.search.filmssearch.R.drawable.luca,
                getString(film.search.filmssearch.R.string.luca_title),
                getString(film.search.filmssearch.R.string.luca_description)
            ),
            Film(
                film.search.filmssearch.R.drawable.monsters_inc,
                getString(film.search.filmssearch.R.string.monsters_inc_title),
                getString(film.search.filmssearch.R.string.monsters_inc_description)
            ),
            Film(
                film.search.filmssearch.R.drawable.onward,
                getString(film.search.filmssearch.R.string.onward_title),
                getString(film.search.filmssearch.R.string.onward_description)
            ),
            Film(
                film.search.filmssearch.R.drawable.ratatouille,
                getString(film.search.filmssearch.R.string.ratatouille_title),
                getString(film.search.filmssearch.R.string.ratatouille_description)
            ),
            Film(
                film.search.filmssearch.R.drawable.soul,
                getString(film.search.filmssearch.R.string.soul_title),
                getString(film.search.filmssearch.R.string.soul_description)
            ),
            Film(
                film.search.filmssearch.R.drawable.toy_story,
                getString(film.search.filmssearch.R.string.toy_story_title),
                getString(film.search.filmssearch.R.string.toy_story_description)
            ),
            Film(
                film.search.filmssearch.R.drawable.toy_story_four,
                getString(film.search.filmssearch.R.string.toy_story_4_title),
                getString(film.search.filmssearch.R.string.toy_story_4_description)
            ),
            Film(
                film.search.filmssearch.R.drawable.walle,
                getString(film.search.filmssearch.R.string.walle_title),
                getString(film.search.filmssearch.R.string.walle_description)
            )
        )
    }

    // Check back pressed. If 2 times in less than 2 sec om main screen - exit. If not main screen (fragment) - back.
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                onBackPressedDispatcher.onBackPressed()
                finish()
            } else {
                Toast.makeText(this, getString(film.search.filmssearch.R.string.double_tap_toast), Toast.LENGTH_SHORT)
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
                film.search.filmssearch.R.id.settings -> {
                    Toast.makeText(this, film.search.filmssearch.R.string.settings_toast, Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                film.search.filmssearch.R.id.favorites -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_placeholder, FavoritesFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }

                film.search.filmssearch.R.id.watch_later -> {
                    Toast.makeText(this, film.search.filmssearch.R.string.watch_later_toast, Toast.LENGTH_SHORT).show()
                    true
                }

                film.search.filmssearch.R.id.collections -> {
                    Toast.makeText(this, film.search.filmssearch.R.string.collection_toast, Toast.LENGTH_SHORT).show()
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
