package com.example.dictionary.network.searchSuggestions.model

import com.example.dictionary.domain.model.rhyme.Rhyme
import com.example.dictionary.domain.model.searchSuggestion.SearchSuggestion
import com.example.dictionary.domain.util.DomainMapper
import com.example.dictionary.network.rhyme.model.RhymeDto

class SearchSuggestionDtoMapper: DomainMapper<SearchSuggestionDto, SearchSuggestion> {

    override fun mapToDomainModel(model: SearchSuggestionDto): SearchSuggestion {
        return SearchSuggestion(
            word = model.word,
            score = model.score,
        )
    }

    override fun mapFromDomainModel(domainModel: SearchSuggestion): SearchSuggestionDto {
        return SearchSuggestionDto(
            word = domainModel.word,
            score = domainModel.score,
        )
    }

    fun toDomainList(initial: List<SearchSuggestionDto>): List<SearchSuggestion> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<SearchSuggestion>): List<SearchSuggestionDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}