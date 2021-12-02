package com.dynamicdal.dictionary.network.rhyme.model

import com.dynamicdal.dictionary.domain.model.rhyme.Rhyme
import com.dynamicdal.dictionary.domain.util.DomainMapper

class RhymeDtoMapper: DomainMapper<RhymeDto, Rhyme> {

    override fun mapToDomainModel(model: RhymeDto): Rhyme {
        return Rhyme(
            word = model.word,
            score = model.score,
            numSyllables = model.numSyllables,
        )
    }

    override fun mapFromDomainModel(domainModel: Rhyme): RhymeDto {
        return RhymeDto(
            word = domainModel.word,
            score = domainModel.score,
            numSyllables = domainModel.numSyllables,
        )
    }

    fun toDomainList(initial: List<RhymeDto>): List<Rhyme> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Rhyme>): List<RhymeDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}