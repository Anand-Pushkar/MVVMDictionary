package com.example.dictionary.di

import com.example.dictionary.cache.definition.DefinitionDao
import com.example.dictionary.cache.definition.model.DefinitionEntityMapper
import com.example.dictionary.interactors.definition_detail_screen.AddToFavoriteWords
import com.example.dictionary.interactors.definition_detail_screen.GetDefinitions
import com.example.dictionary.interactors.definition_detail_screen.RemoveFromFavoriteWords
import com.example.dictionary.interactors.rhyme_detail_screen.GetRhymes
import com.example.dictionary.interactors.search_screen.UpdateSearchSuggestions
import com.example.dictionary.network.WordService
import com.example.dictionary.network.definition.model.DefinitionDtoMapper
import com.example.dictionary.network.rhyme.model.RhymeDtoMapper
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
    fun provideUpdateSearchSuggestions(
        dtoMapper: SearchSuggestionDtoMapper,
        wordService: WordService
    ): UpdateSearchSuggestions {
        return UpdateSearchSuggestions(
            dtoMapper = dtoMapper,
            wordService = wordService
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetDefinitions(
        dtoMapper: DefinitionDtoMapper,
        wordService: WordService,
        entityMapper: DefinitionEntityMapper,
        definitionDao: DefinitionDao
    ): GetDefinitions {
        return GetDefinitions(
            dtoMapper = dtoMapper,
            wordService = wordService,
            entityMapper = entityMapper,
            definitionDao = definitionDao
        )
    }

    @ViewModelScoped
    @Provides
    fun provideAddToFavoriteWords(
        entityMapper: DefinitionEntityMapper,
        definitionDao: DefinitionDao
    ): AddToFavoriteWords {
        return AddToFavoriteWords(
            entityMapper = entityMapper,
            definitionDao = definitionDao
        )
    }

    @ViewModelScoped
    @Provides
    fun provideRemoveFromFavoriteWords(
        dtoMapper: DefinitionDtoMapper,
        wordService: WordService,
        entityMapper: DefinitionEntityMapper,
        definitionDao: DefinitionDao
    ): RemoveFromFavoriteWords {
        return RemoveFromFavoriteWords(
            dtoMapper = dtoMapper,
            wordService = wordService,
            entityMapper = entityMapper,
            definitionDao = definitionDao
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetRhymes(
        dtoMapper: RhymeDtoMapper,
        wordService: WordService,
    ): GetRhymes {
        return GetRhymes(
            dtoMapper = dtoMapper,
            wordService = wordService
        )
    }
}