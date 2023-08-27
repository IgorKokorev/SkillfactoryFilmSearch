package film.search.filmsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import film.search.filmssearch.databinding.FilmItemBinding

// Adapter for RecyclerView with list of films
class FilmRecyclerAdapter(private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val films = mutableListOf<Film>()

    override fun getItemCount() = films.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilmViewHolder(FilmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FilmViewHolder -> {
                holder.binding.poster.setImageResource(films[position].poster)
                holder.binding.title.text = films[position].title
                holder.binding.description.text = films[position].description
                holder.binding.itemContainer.setOnClickListener {
                    clickListener.click(films[position])
                }
            }
        }
    }

    fun addItems(newList: MutableList<Film>) {
        val numbersDiff = FilmsDiff(films, newList)
        val diffResult = DiffUtil.calculateDiff(numbersDiff)
        films.clear()
        films.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }


    //Интерфейс для обработки кликов
    interface OnItemClickListener {
        fun click(film: Film)
    }
}
