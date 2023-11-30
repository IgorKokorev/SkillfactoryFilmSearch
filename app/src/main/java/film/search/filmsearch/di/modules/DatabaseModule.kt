package film.search.filmsearch.di.modules

import dagger.Module
import dagger.Provides
import film.search.filmsearch.domain.MainRepository
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideRepository() = MainRepository()
}