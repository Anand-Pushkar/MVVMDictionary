package com.example.dictionary.cache

import com.example.dictionary.cache.definition.entites.DefinitionEntity
import com.example.dictionary.cache.definition.response.DefinitionEntityMinimalResponse
import com.example.dictionary.cache.rhyme.entities.MyRhymeEntity
import com.example.dictionary.cache.rhyme.entities.RhymeEntity
import com.example.dictionary.cache.rhyme.relations.RhymeZoneCrossRef
import com.example.dictionary.cache.rhyme.response.WordWithAllTheRhymesResponse

class AppDatabaseFake {

    /**
     * The idea is that this represents the cache.
     * Our cache for definitions a single table myWords.
     * Our cache for rhymes are 3 tables :- rhymes, myRhymes, crossRef
     */

    val myWords = mutableListOf<DefinitionEntity>()

    val rhymes = mutableListOf<RhymeEntity>()
    val myRhymes = mutableListOf<MyRhymeEntity>()
    val crossRef = mutableListOf<RhymeZoneCrossRef>()


}