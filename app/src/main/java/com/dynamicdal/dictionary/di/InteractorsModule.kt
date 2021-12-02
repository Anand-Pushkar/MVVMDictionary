package com.dynamicdal.dictionary.di

import com.dynamicdal.dictionary.cache.definition.DefinitionDao
import com.dynamicdal.dictionary.cache.definition.mapper.DefinitionEntityMapper
import com.dynamicdal.dictionary.cache.rhyme.RhymeDao
import com.dynamicdal.dictionary.cache.rhyme.mapper.RhymeEntityMapper
import com.dynamicdal.dictionary.interactors.definition_detail_screen.AddToFavoriteWords
import com.dynamicdal.dictionary.interactors.definition_detail_screen.GetDefinitions
import com.dynamicdal.dictionary.interactors.definition_detail_screen.RemoveFromFavoriteWords
import com.dynamicdal.dictionary.interactors.my_rhymes_screen.GetFavoriteRhymes
import com.dynamicdal.dictionary.interactors.my_words_screen.GetFavoriteWords
import com.dynamicdal.dictionary.interactors.rhyme_detail_screen.AddToFavoriteRhymes
import com.dynamicdal.dictionary.interactors.rhyme_detail_screen.GetRhymes
import com.dynamicdal.dictionary.interactors.rhyme_detail_screen.RemoveFromFavoriteRhymes
import com.dynamicdal.dictionary.interactors.search_screen.UpdateSearchSuggestions
import com.dynamicdal.dictionary.network.WordService
import com.dynamicdal.dictionary.network.definition.model.DefinitionDtoMapper
import com.dynamicdal.dictionary.network.rhyme.model.RhymeDtoMapper
import com.dynamicdal.dictionary.network.searchSuggestions.model.SearchSuggestionDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    // search screen
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

    // definition detail screen
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

    // definition detail screen
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

    // definition detail screen
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

    // rhyme detail screen
    @ExperimentalStdlibApi
    @ViewModelScoped
    @Provides
    fun provideGetRhymes(
        dtoMapper: RhymeDtoMapper,
        wordService: WordService,
        entityMapper: RhymeEntityMapper,
        rhymeDao: RhymeDao
    ): GetRhymes {
        return GetRhymes(
            dtoMapper = dtoMapper,
            wordService = wordService,
            entityMapper = entityMapper,
            rhymeDao = rhymeDao
        )
    }

    // rhyme detail screen
    @ExperimentalStdlibApi
    @ViewModelScoped
    @Provides
    fun provideAddToFavoriteRhymes(
        entityMapper: RhymeEntityMapper,
        rhymeDao: RhymeDao
    ): AddToFavoriteRhymes {
        return AddToFavoriteRhymes(
            entityMapper = entityMapper,
            rhymeDao = rhymeDao
        )
    }

    // rhyme detail screen
    @ExperimentalStdlibApi
    @ViewModelScoped
    @Provides
    fun provideRemoveFromFavoriteRhymes(
        dtoMapper: RhymeDtoMapper,
        wordService: WordService,
        entityMapper: RhymeEntityMapper,
        rhymeDao: RhymeDao
    ): RemoveFromFavoriteRhymes {
        return RemoveFromFavoriteRhymes(
            dtoMapper = dtoMapper,
            wordService = wordService,
            entityMapper = entityMapper,
            rhymeDao = rhymeDao
        )
    }

    // my words screen
    @ViewModelScoped
    @Provides
    fun provideGetFavoriteWords(
        entityMapper: DefinitionEntityMapper,
        definitionDao: DefinitionDao
    ): GetFavoriteWords {
        return GetFavoriteWords(
            entityMapper = entityMapper,
            definitionDao = definitionDao
        )
    }

    // my rhymes screen
    @ExperimentalStdlibApi
    @ViewModelScoped
    @Provides
    fun provideGetFavoriteRhymes(
        entityMapper: RhymeEntityMapper,
        rhymeDao: RhymeDao
    ): GetFavoriteRhymes {
        return GetFavoriteRhymes(
            entityMapper = entityMapper,
            rhymeDao = rhymeDao
        )
    }
}