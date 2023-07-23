package film.search.filmssearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMenuButtons()
        initPostersToasts()
    }

    private fun initPostersToasts() {
        findViewById<ImageView>(R.id.monsters_inc_poster).setOnClickListener() {
            Toast.makeText(this, R.string.monsters_inc, Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageView>(R.id.finding_nemo_poster).setOnClickListener() {
            Toast.makeText(this, R.string.finding_nemo, Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageView>(R.id.ratatouille_poster).setOnClickListener() {
            Toast.makeText(this, R.string.ratatouille, Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageView>(R.id.toy_story_poster).setOnClickListener() {
            Toast.makeText(this, R.string.toy_story, Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageView>(R.id.incredibles_poster).setOnClickListener() {
            Toast.makeText(this, R.string.incredibles, Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageView>(R.id.walle_poster).setOnClickListener() {
            Toast.makeText(this, R.string.walle, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initMenuButtons() {
        findViewById<Button>(R.id.button_menu).setOnClickListener() {
            Toast.makeText(this, R.string.button_menu_toast, Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.button_favorites).setOnClickListener() {
            Toast.makeText(this, R.string.button_favorites_toast, Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.button_watchlist).setOnClickListener() {
            Toast.makeText(this, R.string.button_watchlist_toast, Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.button_info).setOnClickListener() {
            Toast.makeText(this, R.string.button_info_toast, Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.button_settings).setOnClickListener() {
            Toast.makeText(this, R.string.button_settings_toast, Toast.LENGTH_SHORT).show()
        }

    }
}