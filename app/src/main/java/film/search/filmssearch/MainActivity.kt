package film.search.filmssearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMenuButtons()
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