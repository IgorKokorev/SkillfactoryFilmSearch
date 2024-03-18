package film.search.filmsearch.view

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import film.search.filmsearch.Constants
import film.search.filmsearch.R
import film.search.filmsearch.data.entity.Film
import film.search.filmsearch.databinding.ActivityMainBinding
import film.search.filmsearch.utils.BatteryReceiver
import film.search.filmsearch.view.fragments.FavoritesFragment
import film.search.filmsearch.view.fragments.FilmDetailsFragment
import film.search.filmsearch.view.fragments.MainFragment
import film.search.filmsearch.view.fragments.SettingsFragment
import film.search.filmsearch.view.fragments.WatchLaterFragment
import film.search.retrofit.entity.ApiConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var backPressed = 0L
    private val receiver = BatteryReceiver()
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private val ioScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // initialize Firebase remote config
        initFirebaseRemoteConfig()

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
                        intent?.getParcelableExtra(Constants.FILM, Film::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent?.getParcelableExtra(Constants.FILM) as Film?
                    }

        if (film == null) {

            // starting main fragment
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_placeholder, MainFragment())
                .addToBackStack(Constants.FRAGMENT_TAG)
                .commit()
        } else {
            launchDetailsFragment(film)
        }
    }

    // initializing Firebase RemoteConfig and getting the film to show.
    private fun initFirebaseRemoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60 * 60 * 12
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val str = remoteConfig.getString("startup_film")
                    if (str.isBlank()) return@addOnCompleteListener
                    val film: Film
                    try {
                        film = Gson().fromJson(str, Film::class.java)
                    } catch (e: JsonSyntaxException) {
                        Log.d("!!! Firebase RemoteConfig", "Can't parse RemoteConfig data key: \"startup_film\" to film. Received data from FBRC: $str")
                        return@addOnCompleteListener
                    }

                    Glide.with(this)
                        .asDrawable()
                        .load(ApiConstants.IMAGES_URL + "w45" + film.poster)
                        .into(object : CustomTarget<Drawable>() {
                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable>?
                            ) {
                                MaterialAlertDialogBuilder(binding.root.context)
                                    .setTitle(getString(R.string.startup_film_alert_header))
                                    .setMessage(getString(R.string.startup_film_alert_text, film.title))
                                    .setIcon(resource)
                                    .setNegativeButton(getString(R.string.skip)) { _, _ -> }
                                    .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                                        launchDetailsFragment(film)
                                    }
                                    .show()
                            }
                            override fun onLoadCleared(placeholder: Drawable?) {}
                        })

                    Log.d("!!! Firebase RemoteConfig", "Received data from FBRC: $str")
                }
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
            if (backPressed + Constants.BACK_CLICK_TIME_INTERVAL > System.currentTimeMillis()) {
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
                        .addToBackStack(Constants.FRAGMENT_TAG)
                        .commit()
                    true
                }
                R.id.favorites -> {
                    val tag = "favorites"
                    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: FavoritesFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_placeholder, fragment, tag)
                        .addToBackStack(Constants.FRAGMENT_TAG)
                        .commit()
                    true
                }
                R.id.watch_later -> {
                    val tag = "watch later"
                    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: WatchLaterFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_placeholder, fragment, tag)
                        .addToBackStack(Constants.FRAGMENT_TAG)
                        .commit()
                    true
                }
                R.id.settings -> {
                    val tag = "settings"
                    val fragment = supportFragmentManager.findFragmentByTag(tag) ?: SettingsFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_placeholder, fragment, tag)
                        .addToBackStack(Constants.FRAGMENT_TAG)
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
        bundle.putParcelable(Constants.FILM, film)
        val fragment = FilmDetailsFragment()
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(Constants.FRAGMENT_TAG)
            .commit()
    }

}
