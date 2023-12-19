package film.search.filmsearch.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import film.search.filmsearch.App
import film.search.filmsearch.R
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.databinding.ActivityMainBinding
import film.search.filmsearch.databinding.FilmItemBinding
import film.search.filmsearch.view.fragments.FavoritesFragment
import film.search.filmsearch.view.fragments.FilmDetailsFragment
import film.search.filmsearch.view.fragments.MainFragment
import film.search.filmsearch.view.fragments.SettingsFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var backPressed = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // initializing menu buttons click listeners
        initMenuButtons()

        // initializing notification service
//        App.instance.notificationService = NotificationService(this)

        // starting main fragment
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_placeholder, MainFragment())
            .addToBackStack(App.instance.FRAGMENT_TAG)
            .commit()
    }

    // Check back pressed. If 2 times in less than 2 sec om main screen - exit. If not main screen (fragment) - back.
    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount <= 1) {
            if (backPressed + App.instance.BACK_CLICK_TIME_INTERVAL > System.currentTimeMillis()) {
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
                        .addToBackStack(App.instance.FRAGMENT_TAG)
                        .commit()
                    true
                }
                R.id.favorites -> {
                    val tag = "favorites"
                    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: FavoritesFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_placeholder, fragment, tag)
                        .addToBackStack(App.instance.FRAGMENT_TAG)
                        .commit()
                    true
                }

                R.id.settings -> {
                    val tag = "settings"
                    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: SettingsFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_placeholder, fragment, tag)
                        .addToBackStack(App.instance.FRAGMENT_TAG)
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
        bundle.putParcelable(App.instance.FILM, film)
        bundle.putInt(App.instance.POSITION, position)
        val fragment = FilmDetailsFragment()
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(App.instance.FRAGMENT_TAG)
            .commit()
    }

}
