package com.example.dictionary.di

import com.example.dictionary.network.definition.model.DefinitionDtoMapper
import com.example.dictionary.network.rhyme.model.RhymeDtoMapper
import com.example.dictionary.network.searchSuggestions.model.SearchSuggestionDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideDefinitionMapper(): DefinitionDtoMapper{
        return DefinitionDtoMapper()
    }

    @Singleton
    @Provides
    fun provideRhymeMapper(): RhymeDtoMapper {
        return RhymeDtoMapper()
    }

    @Singleton
    @Provides
    fun provideSearchSuggestionMapper(): SearchSuggestionDtoMapper {
        return SearchSuggestionDtoMapper()
    }

    @Singleton
    @Provides
    @Named("meta_data")
    fun provideMetaData(): String{
        return "ds"
    }
}