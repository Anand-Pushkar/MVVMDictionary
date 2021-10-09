package com.example.dictionary.cache.definition.model

import com.example.dictionary.domain.model.definition.Definition
import com.example.dictionary.domain.model.definition.DefinitionMinimal
import com.example.dictionary.domain.util.DomainMapper

class DefinitionEntityMapper: DomainMapper<DefinitionEntity, Definition> {

    override fun mapToDomainModel(model: DefinitionEntity): Definition {
        return Definition(
            word = model.word,
            score = model.score,
            numSyllables = model.numSyllables,
            tags = convertStringToList(model.tags),
            defs = convertStringToList(model.defs),
            isFavorite = model.isFavorite
        )
    }

    // for MyWords we only need what DefinitionMinimal holds,
    // so we will be mapping from DefinitionEntity to DefinitionMinimal.
    fun mapToDomainMinimalModel(model: DefinitionEntityMinimal): DefinitionMinimal {
        return DefinitionMinimal(
            word = model.word,
            pronunciation = model.pronunciation,
            statement = model.statement
        )
    }

    override fun mapFromDomainModel(domainModel: Definition): DefinitionEntity {
        return DefinitionEntity(
            word = domainModel.word,
            score = domainModel.score,
            numSyllables = domainModel.numSyllables,
            tags = convertListToString(domainModel.tags),
            defs = convertListToString(domainModel.defs),
            pronunciation = domainModel.pronunciation,
            statement = domainModel.statement,
            isFavorite = true // should always be true if the word is added to the cache
        )
    }

    fun toDomainList(initial: List<DefinitionEntity>): List<Definition>{
        return initial.map { mapToDomainModel(it) }
    }

    fun toDomainMinimalList(initial: List<DefinitionEntityMinimal>): List<DefinitionMinimal>{
        return initial.map { mapToDomainMinimalModel(it) }
    }

    fun fromDomainList(initial: List<Definition>): List<DefinitionEntity>{
        return initial.map { mapFromDomainModel(it) }
    }


    // ["Carrot", "Potato", "Chicken", ...] -> "Carrot|Potato|Chicken| ..."
    private fun convertListToString(mList: List<String>?): String{
        val mString = StringBuilder()
        if (mList != null) {
            val listSize = mList.size
            mList.forEachIndexed{ index, it ->
                if(index == listSize - 1){
                    mString.append(it)
                }else{
                    mString.append("$it|")
                }
            }
        }
        return mString.toString()
    }

    // "Carrot|Potato|Chicken| ..." -> ["Carrot", "Potato", "Chicken", ...]
    private fun convertStringToList(mString: String?): List<String>{
        val list: ArrayList<String> = ArrayList()
        mString?.let { mString ->
            for(mVal in mString.split("|")){
                list.add(mVal)
            }
        }
        return list
    }
}