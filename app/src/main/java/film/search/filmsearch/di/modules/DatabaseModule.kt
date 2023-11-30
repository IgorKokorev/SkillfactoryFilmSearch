package film.search.filmsearch.di.modules

import dagger.Binds
import dagger.Module
import javax.inject.Inject
import javax.inject.Singleton

@Module
abstract class DatabaseModule {
    @Binds
    @Singleton
    abstract fun getRepository(repository: MainRepository) : FilmRepositoryInterface
}

interface FilmRepositoryInterface
class MainRepository @Inject constructor() : FilmRepositoryInterface