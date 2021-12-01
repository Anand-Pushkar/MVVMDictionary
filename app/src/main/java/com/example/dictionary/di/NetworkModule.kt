package com.example.dictionary.di

import com.example.dictionary.network.WordService
import com.example.dictionary.network.definition.model.DefinitionDtoMapper
import com.example.dictionary.network.rhyme.model.RhymeDtoMapper
import com.example.dictionary.network.searchSuggestions.model.SearchSuggestionDtoMapper
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideWordService(): WordService{
        return Retrofit.Builder()
            .baseUrl("https://api.datamuse.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(WordService::class.java)
    }

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
        return "dsr"
    }
}