package com.dynamicdal.dictionary.cache.rhyme.response

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.dynamicdal.dictionary.cache.rhyme.entities.MyRhymeEntity
import com.dynamicdal.dictionary.cache.rhyme.entities.RhymeEntity
import com.dynamicdal.dictionary.cache.rhyme.relations.RhymeZoneCrossRef

data class RhymeWithAllTheWordsResponse(
    @Embedded
    val rhyme: RhymeEntity,

    @Relation(
        parentColumn = "word",
        entityColumn = "mainWord",
        associateBy = Junction(RhymeZoneCrossRef::class)
    )
    val cWords: List<MyRhymeEntity>
)
