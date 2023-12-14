package film.search.filmsearch.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import film.search.filmsearch.data.AppDatabase
import film.search.filmsearch.data.DAO.FilmDao
import film.search.filmsearch.data.MainRepository
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideFilmDao(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "film_db"
        ).build().filmDao()

    @Singleton
    @Provides
    fun provideRepository(filmDao: FilmDao) = MainRepository(filmDao)
}