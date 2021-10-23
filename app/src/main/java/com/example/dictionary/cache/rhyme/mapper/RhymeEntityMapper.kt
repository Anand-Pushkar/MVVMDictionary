package com.example.dictionary.cache.rhyme.mapper

import com.example.dictionary.cache.rhyme.entities.MyRhymeEntity
import com.example.dictionary.cache.rhyme.entities.RhymeEntity
import com.example.dictionary.cache.rhyme.relations.RhymeZoneCrossRef
import com.example.dictionary.cache.rhyme.response.WordWithAllTheRhymesResponse
import com.example.dictionary.domain.model.rhyme.Rhyme
import com.example.dictionary.domain.model.rhyme.Rhymes
import com.example.dictionary.domain.model.rhyme.RhymesMinimal
import com.example.dictionary.domain.util.DomainMapper

@ExperimentalStdlibApi
class RhymeEntityMapper: DomainMapper<RhymeEntity, Rhyme> {

    override fun mapToDomainModel(model: RhymeEntity): Rhyme {
        return Rhyme(
            word = model.word,
            score = model.score,
            numSyllables = model.numSyllables
        )
    }

    override fun mapFromDomainModel(domainModel: Rhyme): RhymeEntity {
        return RhymeEntity(
            word = domainModel.word,
            score = domainModel.score,
            numSyllables = domainModel.numSyllables
        )
    }



    // for MyRhymes screen
    fun mapToDomainMinimalModel(model: MyRhymeEntity): RhymesMinimal {
        return RhymesMinimal(
            word = model.mainWord,
            syllableInfo = model.syllableInfo
        )
    }



    // for RhymeDetailScreen
    fun mapToRhymesModel(model: WordWithAllTheRhymesResponse): Rhymes {
        return Rhymes(
            mainWord = model.myRhyme.mainWord,
            rhymeList = toDomainList(model.relatedRhymes),
            isFavorite = model.myRhyme.isFavorite
        )
    }

    fun mapFromRhymesModel(domainModel: Rhymes): MyRhymeEntity {
        return MyRhymeEntity(
            mainWord = domainModel.mainWord,
            syllableInfo = domainModel.syllableInfo,
            isFavorite = true // should always be true if the word is added to the cache
        )
    }


    fun mapToRhymeZoneCrossRef(mainWord: String, word: String): RhymeZoneCrossRef {
        return RhymeZoneCrossRef(
            mainWord = mainWord,
            word = word
        )
    }

    fun toDomainList(initial: List<RhymeEntity>): List<Rhyme>{
        return initial.map { mapToDomainModel(it) }
    }

    fun toDomainMinimalList(initial: List<MyRhymeEntity>): List<RhymesMinimal>{
        return initial.map { mapToDomainMinimalModel(it) }
    }

    fun fromDomainList(initial: List<Rhyme>): List<RhymeEntity >{
        return initial.map { mapFromDomainModel(it) }
    }
}

