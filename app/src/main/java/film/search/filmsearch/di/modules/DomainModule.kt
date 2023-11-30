package film.search.filmsearch.di.modules

import dagger.Module
import dagger.Provides
import film.search.filmsearch.data.tmbd.TmdbApi
import film.search.filmsearch.domain.Interactor
import film.search.filmsearch.domain.MainRepository
import javax.inject.Singleton

@Module
class DomainModule {
    @Singleton
    @Provides
    fun provideInteractor(repository: MainRepository, tmdbApi: TmdbApi) = Interactor(repo = repository, retrofitService = tmdbApi)
}