package film.search.filmsearch.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import film.search.filmsearch.data.AppDatabase
import film.search.filmsearch.data.DAO.FavoriteFilmDao
import film.search.filmsearch.data.DAO.FilmDao
import film.search.filmsearch.data.MainRepository
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "film_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    @Singleton
    @Provides
    fun provideFilmDao(db: AppDatabase): FilmDao = db.filmDao()

    @Singleton
    @Provides
    fun provideFavoriteFilmDao(db: AppDatabase): FavoriteFilmDao = db.favoriteFilmsDao()

    @Singleton
    @Provides
    fun provideRepository(filmDao: FilmDao, favoriteFilmDao: FavoriteFilmDao) =
        MainRepository(filmDao, favoriteFilmDao)
}