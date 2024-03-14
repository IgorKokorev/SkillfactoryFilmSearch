package film.search.filmsearch.di

import dagger.Component
import film.search.filmsearch.di.modules.DatabaseModule
import film.search.filmsearch.di.modules.DomainModule
import film.search.filmsearch.utils.AlarmService
import film.search.filmsearch.utils.WatchLaterReminderReceiver
import film.search.filmsearch.viewmodel.FavoritesFragmentViewModel
import film.search.filmsearch.viewmodel.FilmDetailsFragmentViewModel
import film.search.filmsearch.viewmodel.MainFragmentViewModel
import film.search.filmsearch.viewmodel.SettingsFragmentViewModel
import film.search.filmsearch.viewmodel.WatchLaterFragmentViewModel
import film.search.retrofit.RemoteProvider
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [RemoteProvider::class],
    modules = [
        DatabaseModule::class,
        DomainModule::class
    ]
)
interface AppComponent {
    fun inject(mainFragmentViewModel: MainFragmentViewModel)
    fun inject(filmDetailsFragmentViewModel: FilmDetailsFragmentViewModel)
    fun inject(favoritesFragmentViewModel: FavoritesFragmentViewModel)
    fun inject(settingsFragmentViewModel: SettingsFragmentViewModel)
    fun inject(watchLaterFragmentViewModel: WatchLaterFragmentViewModel)
    fun inject(alarmService: AlarmService)
    fun inject(watchLaterReminderReceiver: WatchLaterReminderReceiver)
}