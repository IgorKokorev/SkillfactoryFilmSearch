package film.search.filmsearch.view.rvadapters

import androidx.recyclerview.widget.DiffUtil
import film.search.filmsearch.data.entity.WatchLaterFilm

// Calculates difference between 2 film lists for DiffUtil
class WatchLaterFilmsDiff(val oldList: MutableList<WatchLaterFilm>, val newList: List<WatchLaterFilm>) :
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
