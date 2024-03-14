package film.search.filmsearch.view.rvadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import film.search.filmsearch.data.entity.WatchLaterFilm
import film.search.filmsearch.databinding.WatchLaterItemBinding
import film.search.filmsearch.view.rvviewholders.WatchLaterFilmViewHolder

// Adapter for RecyclerView with list of films
class WatchLaterFilmRecyclerAdapter(private val clickListener: OnWatchLaterItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val watchLaterFilms = mutableListOf<WatchLaterFilm>()

    override fun getItemCount() = watchLaterFilms.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WatchLaterFilmViewHolder(WatchLaterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    // initializing holder's data for each film
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WatchLaterFilmViewHolder -> {
                val watchLaterFilm = watchLaterFilms[position]
                holder.setData(watchLaterFilm, clickListener, position)
            }
        }
    }

    // initializing adapter's database with given list. Using DiffUtil for changes
    fun addItems(newList: List<WatchLaterFilm>) {
        val numbersDiff = WatchLaterFilmsDiff(watchLaterFilms, newList)
        val diffResult = DiffUtil.calculateDiff(numbersDiff)
        watchLaterFilms.clear()
        watchLaterFilms.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
/*
    fun clearFilms() {
        watchLaterFilms.clear()
    }*/

    // interface for on-film clicks handler
    interface OnWatchLaterItemClickListener {
        fun click(watchLaterFilm: WatchLaterFilm)
        fun deleteClick(watchLaterFilm: WatchLaterFilm)
    }
}
