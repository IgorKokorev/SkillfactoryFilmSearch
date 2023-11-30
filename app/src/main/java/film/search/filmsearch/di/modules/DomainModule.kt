package film.search.filmsearch.di.modules

import dagger.Binds
import dagger.Module
import film.search.filmsearch.domain.Interactor
import javax.inject.Singleton

@Module
abstract class DomainModule {
    @Singleton
    @Binds
    abstract fun getInteractor(interactor: Interactor) : InteractorInterface
}

interface InteractorInterface