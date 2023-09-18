package film.search.filmsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import film.search.filmssearch.databinding.FilmItemBinding

// Adapter for RecyclerView with list of films
class FilmRecyclerAdapter(private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val films = mutableListOf<Film>()

    override fun getItemCount() = films.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilmViewHolder(FilmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    // initializing holder's data for each film
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FilmViewHolder -> {
                val film = films[position]
                holder.setData(film, clickListener, position)
            }
        }
    }

    // initializing adapter's database with given list. Using DiffUtil for changes
    fun addItems(newList: MutableList<Film>) {
        val numbersDiff = FilmsDiff(films, newList)
        val diffResult = DiffUtil.calculateDiff(numbersDiff)
        films.clear()
        films.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }


    // interface for on-film clicks handler
    interface OnItemClickListener {
        fun click(film: Film, position: Int, binding: FilmItemBinding)
    }
}
