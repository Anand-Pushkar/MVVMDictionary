package com.example.dictionary.di

import com.example.dictionary.interactors.search_screen.UpdateSearchSuggestion
import com.example.dictionary.network.WordService
import com.example.dictionary.network.searchSuggestions.model.SearchSuggestionDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideUpdateSearchSuggestion(
        dtoMapper: SearchSuggestionDtoMapper,
        wordService: WordService
    ): UpdateSearchSuggestion {
        return UpdateSearchSuggestion(
            dtoMapper = dtoMapper,
            wordService = wordService
        )
    }



}