package film.search.filmsearch.domain

import film.search.filmsearch.data.MainRepository

class Interactor(val repo: MainRepository) {
    fun getFilmsDB(): List<Film> = repo.allFilms
    fun getFavoriteFilms() : List<Film> = repo.allFilms.filter { film -> film.isFavorite }
}
