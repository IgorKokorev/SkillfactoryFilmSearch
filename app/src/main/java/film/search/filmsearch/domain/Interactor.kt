package film.search.filmsearch.domain

import film.search.filmsearch.data.MainRepository

// class to interact with film db
class Interactor(val repo: MainRepository) {
    fun getFilmsDB(): List<Film> = repo.allFilms
}
