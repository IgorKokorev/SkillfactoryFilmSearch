package film.search.filmsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import film.search.filmssearch.R
import film.search.filmssearch.databinding.ActivityFilmDetailsBinding

class FilmDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilmDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val film = intent.extras?.get("film") as Film

        binding.detailsToolbar.title = film.title
        binding.detailsPoster.setImageResource(film.poster)
        binding.detailsDescription.text = film.description

        initBottomMenuButtons(film.title)


    }

    private fun initBottomMenuButtons(title: String) {
        val view: View = findViewById(android.R.id.content)
        val snackbarFavorite = Snackbar.make(view, R.string.favorites_toast, Snackbar.LENGTH_SHORT)
        val snackbarWatchLater = Snackbar.make(view, R.string.watch_later_toast, Snackbar.LENGTH_SHORT)
        val snackbarCollections = Snackbar.make(view, R.string.collection_toast, Snackbar.LENGTH_SHORT)

        binding.detailsFab.setOnClickListener {
            Snackbar.make(view, String.format(getString(R.string.send_to_friend), title), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.share), {

                }).show()
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favorites -> {
                    snackbarFavorite.show()
                    true
                }

                R.id.watch_later -> {
                    snackbarWatchLater.show()
                    true
                }

                R.id.collections -> {
                    snackbarCollections.show()
                    true
                }

                else -> false
            }
        }
    }
}