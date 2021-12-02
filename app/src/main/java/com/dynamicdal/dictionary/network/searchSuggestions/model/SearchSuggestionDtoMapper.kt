package com.dynamicdal.dictionary.network.searchSuggestions.model

import com.dynamicdal.dictionary.domain.model.searchSuggestion.SearchSuggestion
import com.dynamicdal.dictionary.domain.util.DomainMapper

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