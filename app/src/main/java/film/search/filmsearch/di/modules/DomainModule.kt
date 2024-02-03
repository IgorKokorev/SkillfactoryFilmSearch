package film.search.filmsearch.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import film.search.filmsearch.data.MainRepository
import film.search.filmsearch.data.PreferenceProvider
import film.search.filmsearch.domain.Interactor
import javax.inject.Singleton

@Module
class DomainModule(val context: Context) {
    @Provides
    fun provideContext() = context

    @Singleton
    @Provides
    fun providePreferences(context: Context) = PreferenceProvider(context)

    @Singleton
    @Provides
    fun provideInteractor(repository: MainRepository, tmdbApi: film.search.retrofit.TmdbApi, preferenceProvider: PreferenceProvider) =
        Interactor(repo = repository, retrofitService = tmdbApi, preferences = preferenceProvider)
}