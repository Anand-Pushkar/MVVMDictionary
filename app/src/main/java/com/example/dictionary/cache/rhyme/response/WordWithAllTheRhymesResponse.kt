package com.example.dictionary.cache.rhyme.response

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.dictionary.cache.rhyme.entities.MyRhymeEntity
import com.example.dictionary.cache.rhyme.entities.RhymeEntity
import com.example.dictionary.cache.rhyme.relations.RhymeZoneCrossRef

data class WordWithAllTheRhymesResponse(
    @Embedded
    val myRhyme: MyRhymeEntity,

    @Relation(
        parentColumn = "mainWord",
        entityColumn = "word",
        associateBy = Junction(RhymeZoneCrossRef::class)
    )
    val relatedRhymes: List<RhymeEntity>
)
