package com.example.dictionary.cache.database

import androidx.annotation.NonNull
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dictionary.cache.definition.DefinitionDao
import com.example.dictionary.cache.definition.model.DefinitionEntity
import androidx.sqlite.db.SupportSQLiteDatabase

import androidx.room.migration.Migration




@Database(entities = [DefinitionEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun definitionDao(): DefinitionDao

    companion object{
        val DATABASE_NAME = "app_db"
    }


    @VisibleForTesting
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(@NonNull database: SupportSQLiteDatabase) {
        }
    }
}