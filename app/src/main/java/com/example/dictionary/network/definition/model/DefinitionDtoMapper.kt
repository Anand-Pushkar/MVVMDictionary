package com.example.dictionary.network.definition.model

import com.example.dictionary.domain.model.definition.Definition
import com.example.dictionary.domain.util.DomainMapper

class DefinitionDtoMapper: DomainMapper<DefinitionDto, Definition> {

    override fun mapToDomainModel(model: DefinitionDto): Definition {
        return Definition(
            word = model.word,
            score = model.score,
            numSyllables = model.numSyllables,
            tags = model.tags,
            defs = model.defs
        )
    }

    override fun mapFromDomainModel(domainModel: Definition): DefinitionDto {
        return DefinitionDto(
            word = domainModel.word,
            score = domainModel.score,
            numSyllables = domainModel.numSyllables,
            tags = domainModel.tags,
            defs = domainModel.defs
        )
    }

    fun toDomainList(initial: List<DefinitionDto>): List<Definition> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Definition>): List<DefinitionDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}