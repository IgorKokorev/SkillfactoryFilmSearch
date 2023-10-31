package film.search.filmsearch.view.rvadapters

import androidx.recyclerview.widget.DiffUtil
import film.search.filmsearch.domain.Film

// Calculates difference between 2 films for DiffUtil
class FilmsDiff(val oldList: MutableList<Film>, val newList: List<Film>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].equals(newList[newItemPosition])
    }
}
