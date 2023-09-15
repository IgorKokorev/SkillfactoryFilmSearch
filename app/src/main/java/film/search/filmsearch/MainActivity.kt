package film.search.filmsearch

import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import film.search.filmssearch.R
import film.search.filmssearch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var backPressed = 0L

    override fun onCreate(savedInstanceState: Bundle?) {

/*        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.allowEnterTransitionOverlap = true
        window.allowReturnTransitionOverlap = true*/

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

        // starting main fragment
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_placeholder, MainFragment())
            .addToBackStack(null)
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
                getString(R.string.finding_nemo_title),
                getString(R.string.finding_nemo_description)
            ),
            Film(
                R.drawable.incredibles,
                getString(R.string.incredibles_title),
                getString(R.string.incredibles_description)
            ),
            Film(
                R.drawable.lightyear,
                getString(R.string.lightyear_title),
                getString(R.string.lightyear_description)
            ),
            Film(
                R.drawable.luca,
                getString(R.string.luca_title),
                getString(R.string.luca_description)
            ),
            Film(
                R.drawable.monsters_inc,
                getString(R.string.monsters_inc_title),
                getString(R.string.monsters_inc_description)
            ),
            Film(
                R.drawable.onward,
                getString(R.string.onward_title),
                getString(R.string.onward_description)
            ),
            Film(
                R.drawable.ratatouille,
                getString(R.string.ratatouille_title),
                getString(R.string.ratatouille_description)
            ),
            Film(
                R.drawable.soul,
                getString(R.string.soul_title),
                getString(R.string.soul_description)
            ),
            Film(
                R.drawable.toy_story,
                getString(R.string.toy_story_title),
                getString(R.string.toy_story_description)
            ),
            Film(
                R.drawable.toy_story_four,
                getString(R.string.toy_story_4_title),
                getString(R.string.toy_story_4_description)
            ),
            Film(
                R.drawable.walle,
                getString(R.string.walle_title),
                getString(R.string.walle_description)
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
                R.id.favorites -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_placeholder, FavoritesFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }

                R.id.watch_later -> {
                    Toast.makeText(this, R.string.watch_later_toast, Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.collections -> {
                    Toast.makeText(this, R.string.collection_toast, Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }

    // Launch Film Details screen (fragment) and transfer film data to it
    fun launchDetailsFragment(film: Film) {
        val bundle = Bundle()
        bundle.putParcelable(FILM, film)
        val fragment = FilmDetailsFragment()
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(null)
            .commit()
    }

    // Contains constants:
    // interval for double tap in ms
    // name of the bundle with film info to transfer to another fragment
    // allFilms - fake film db
    // favoriteFilms - list of favorite films
    companion object {
        const val TIME_INTERVAL = 2000L
        const val FILM = "film"
        lateinit var notificationService: NotificationService
        var allFilms = mutableListOf<Film>()
        val favoriteFilms = mutableListOf<Film>()
    }
}
