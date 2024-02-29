package film.search.filmsearch.view

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import film.search.filmsearch.App
import film.search.filmsearch.R
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.databinding.ActivityMainBinding
import film.search.filmsearch.utils.BatteryReceiver
import film.search.filmsearch.view.fragments.FavoritesFragment
import film.search.filmsearch.view.fragments.FilmDetailsFragment
import film.search.filmsearch.view.fragments.MainFragment
import film.search.filmsearch.view.fragments.SettingsFragment
import film.search.filmsearch.view.fragments.WatchLaterFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var backPressed = 0L
    private val receiver = BatteryReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // initializing menu buttons click listeners
        initMenuButtons()

        // Registering Receiver to catch Low Battery and Power connected events
        val filter = IntentFilter(Intent.ACTION_BATTERY_LOW)
        filter.addAction(Intent.ACTION_POWER_CONNECTED)
        registerReceiver(receiver, filter)

        // initializing notification service
//        App.instance.notificationService = NotificationService(this)

        // Ask for permission to post notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 99)
        }

        // If the activity was launched from an intent we get film from the intent and open it in film datails fragment
        val film =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent?.getParcelableExtra(App.instance.FILM, Film::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent?.getParcelableExtra(App.instance.FILM) as Film?
                    }

        if (film == null) {
            // starting main fragment
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_placeholder, MainFragment())
                .addToBackStack(App.instance.FRAGMENT_TAG)
                .commit()
        } else {
            launchDetailsFragment(film)
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
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
                R.id.watch_later -> {
                    val tag = "watch later"
                    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: WatchLaterFragment()
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
    fun launchDetailsFragment(film: Film) {
        val bundle = Bundle()
        bundle.putParcelable(App.instance.FILM, film)
        val fragment = FilmDetailsFragment()
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(App.instance.FRAGMENT_TAG)
            .commit()
    }

}
