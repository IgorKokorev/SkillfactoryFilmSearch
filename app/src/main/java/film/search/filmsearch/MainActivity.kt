package film.search.filmsearch

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import film.search.filmssearch.R
import film.search.filmssearch.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var filmsAdapter: FilmRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        initMenuButtons()
        initRecycler()


    }

    private fun initRecycler() {

        filmsAdapter = FilmRecyclerAdapter(object : FilmRecyclerAdapter.OnItemClickListener {
            override fun click(film: Film) {
                val bundle = Bundle()
                bundle.putParcelable("film", film)
                val intent = Intent(this@MainActivity, FilmDetailsActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        val films = mutableListOf(
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

        filmsAdapter.addItems(films)

        binding.mainRecycler.adapter = filmsAdapter

        val decorator = TopSpacingItemDecoration(8)
        binding.mainRecycler.addItemDecoration(decorator)
    }

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
                    Toast.makeText(this, R.string.favorites_toast, Toast.LENGTH_SHORT).show()
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
}