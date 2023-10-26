package film.search.filmsearch

import android.os.Bundle
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

//        filmDetailsFragment.sharedElementEnterTransition = AutoTransition().setDuration(1000L)

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
                getString(R.string.brave_description),
                49
            ),
            Film(
                R.drawable.cars,
                getString(R.string.cars_title),
                getString(R.string.cars_description),
                99
            ),
            Film(
                R.drawable.finding_nemo,
                getString(R.string.finding_nemo_title),
                getString(R.string.finding_nemo_description),
                56
            ),
            Film(
                R.drawable.incredibles,
                getString(R.string.incredibles_title),
                getString(R.string.incredibles_description),
                83
            ),
            Film(
                R.drawable.lightyear,
                getString(R.string.lightyear_title),
                getString(R.string.lightyear_description),
                23
            ),
            Film(
                R.drawable.luca,
                getString(R.string.luca_title),
                getString(R.string.luca_description),
                38
            ),
            Film(
                R.drawable.monsters_inc,
                getString(R.string.monsters_inc_title),
                getString(R.string.monsters_inc_description),
                100
            ),
            Film(
                R.drawable.onward,
                getString(R.string.onward_title),
                getString(R.string.onward_description),
                72
            ),
            Film(
                R.drawable.ratatouille,
                getString(R.string.ratatouille_title),
                getString(R.string.ratatouille_description),
                0
            ),
            Film(
                R.drawable.soul,
                getString(R.string.soul_title),
                getString(R.string.soul_description),
                88
            ),
            Film(
                R.drawable.toy_story,
                getString(R.string.toy_story_title),
                getString(R.string.toy_story_description),
                15
            ),
            Film(
                R.drawable.toy_story_four,
                getString(R.string.toy_story_4_title),
                getString(R.string.toy_story_4_description),
                16
            ),
            Film(
                R.drawable.walle,
                getString(R.string.walle_title),
                getString(R.string.walle_description),
                93
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
