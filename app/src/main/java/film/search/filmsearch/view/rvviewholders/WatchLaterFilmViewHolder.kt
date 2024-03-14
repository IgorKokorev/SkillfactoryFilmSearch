package film.search.filmsearch.view.rvviewholders

import androidx.recyclerview.widget.RecyclerView
import film.search.filmsearch.data.entity.WatchLaterFilm
import film.search.filmsearch.databinding.WatchLaterItemBinding
import film.search.filmsearch.view.rvadapters.WatchLaterFilmRecyclerAdapter
import java.text.SimpleDateFormat

// Holder for Recycler view item
class WatchLaterFilmViewHolder(val binding: WatchLaterItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setData(watchLaterFilm: WatchLaterFilm, clickListener: WatchLaterFilmRecyclerAdapter.OnWatchLaterItemClickListener, position: Int) {

        binding.title.text = watchLaterFilm.title
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")
        binding.time.text = simpleDateFormat.format(watchLaterFilm.time)
        binding.time.setOnClickListener {
            clickListener.click(watchLaterFilm)
        }
        binding.deleteButton.setOnClickListener {
            clickListener.deleteClick(watchLaterFilm)
        }
        adapterPosition
    }
}
