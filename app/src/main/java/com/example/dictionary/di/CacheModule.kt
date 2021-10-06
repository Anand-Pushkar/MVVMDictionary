package com.example.dictionary.di

import androidx.room.Room
import androidx.work.impl.WorkDatabaseMigrations.MIGRATION_1_2
import com.example.dictionary.cache.database.AppDatabase
import com.example.dictionary.cache.definition.DefinitionDao
import com.example.dictionary.cache.definition.model.DefinitionEntityMapper
import com.example.dictionary.presentation.BaseApplication
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
    fun provideDefinitionDao(app: AppDatabase): DefinitionDao{
        return app.definitionDao()
    }

    @Singleton
    @Provides fun provideCacheRecipeMapper(): DefinitionEntityMapper{
        return DefinitionEntityMapper()
    }
}