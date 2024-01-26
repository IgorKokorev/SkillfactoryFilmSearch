package film.search.filmsearch.di

import dagger.Component
import film.search.filmsearch.di.modules.DatabaseModule
import film.search.filmsearch.di.modules.DomainModule
import film.search.filmsearch.di.modules.RemoteModule
import film.search.filmsearch.viewmodel.FavoritesFragmentViewModel
import film.search.filmsearch.viewmodel.FilmDetailsFragmentViewModel
import film.search.filmsearch.viewmodel.MainFragmentViewModel
import film.search.filmsearch.viewmodel.SettingsFragmentViewModel
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RemoteModule::class,
        DatabaseModule::class,
        DomainModule::class
    ]
)
interface AppComponent {
    fun inject(mainFragmentViewModel: MainFragmentViewModel)
    fun inject(filmDetailsFragmentViewModel: FilmDetailsFragmentViewModel)
    fun inject(favoritesFragmentViewModel: FavoritesFragmentViewModel)
    fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
}