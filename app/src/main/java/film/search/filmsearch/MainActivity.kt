package film.search.filmsearch

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import film.search.filmssearch.R
import film.search.filmssearch.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        initMenuButtons()

        setupPostersAnimations()
    }

    private fun setupPostersAnimations() {

        val scaleUpAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        val scaleDownAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_down)
        binding.poster1.setOnClickListener() {
            binding.poster1.startAnimation(scaleUpAnimation)
            binding.poster1.startAnimation(scaleDownAnimation)
        }

        val rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.rotation)
        rotationAnimation.interpolator = OvershootInterpolator()
        binding.poster2.setOnClickListener() {
            binding.poster2.startAnimation(rotationAnimation)
        }

        val maxShift = 100f
        binding.poster3.setOnClickListener {
            val dx: Float = Random.nextFloat() * maxShift * 2 - maxShift
            val dy: Float = Random.nextFloat() * maxShift * 2 - maxShift
            binding.poster3.animate()
                .setDuration(500)
                .setInterpolator(BounceInterpolator())
                .translationX(dx)
                .translationY(dy)
                .start()
        }

        binding.poster4.setOnClickListener {
            binding.poster4.animate()
                .setDuration(500)
                .setInterpolator(AnticipateOvershootInterpolator())
                .alpha(Random.nextFloat())
                .scaleX(Random.nextFloat() + 0.5f)
                .scaleY(Random.nextFloat() + 0.5f)
                .start()
        }
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