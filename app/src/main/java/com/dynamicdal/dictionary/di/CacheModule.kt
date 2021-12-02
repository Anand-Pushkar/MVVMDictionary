package com.dynamicdal.dictionary.di

import androidx.room.Room
import com.dynamicdal.dictionary.cache.database.AppDatabase
import com.dynamicdal.dictionary.cache.definition.DefinitionDao
import com.dynamicdal.dictionary.cache.definition.mapper.DefinitionEntityMapper
import com.dynamicdal.dictionary.cache.rhyme.RhymeDao
import com.dynamicdal.dictionary.cache.rhyme.mapper.RhymeEntityMapper
import com.dynamicdal.dictionary.presentation.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideDb(app: BaseApplication): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            //.addMigrations(MIGRATION_1_2) // there is no migrations for now as the version is 1
            .build()
    }

    @Singleton
    @Provides
    fun provideDefinitionDao(app: AppDatabase): DefinitionDao {
        return app.definitionDao()
    }

    @Singleton
    @Provides
    fun provideRhymeDao(app: AppDatabase): RhymeDao {
        return app.rhymeDao()
    }

    @Singleton
    @Provides
    fun provideDefinitionCacheMapper(): DefinitionEntityMapper {
        return DefinitionEntityMapper()
    }

    @ExperimentalStdlibApi
    @Singleton
    @Provides
    fun provideRhymeCacheMapper(): RhymeEntityMapper {
        return RhymeEntityMapper()
    }
}