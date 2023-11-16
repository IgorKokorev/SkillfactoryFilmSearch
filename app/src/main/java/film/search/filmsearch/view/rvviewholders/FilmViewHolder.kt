package film.search.filmsearch.view.rvviewholders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import film.search.filmsearch.App
import film.search.filmsearch.data.tmbd.ApiConstants
import film.search.filmsearch.domain.Film
import film.search.filmsearch.view.rvadapters.FilmRecyclerAdapter
import film.search.filmssearch.databinding.FilmItemBinding

// Holder for Recycler view item
class FilmViewHolder(val binding: FilmItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setData(film: Film, clickListener: FilmRecyclerAdapter.OnItemClickListener, position: Int) {

        // Using Glide for images
        Glide.with(binding.root) // container
            .load(ApiConstants.IMAGES_URL + "w342" + film.poster) // what picture is loaded
            .centerCrop()
            .into(binding.poster) // image view

        binding.poster.transitionName = App.instance.TRANSITION_NAME + position

        binding.title.text = film.title
        binding.description.text = film.description
        binding.root.setOnClickListener {
            clickListener.click(film, position, binding)
        }
        binding.ratingView.setProgress(film.rating.toFloat())
        adapterPosition
    }
}
