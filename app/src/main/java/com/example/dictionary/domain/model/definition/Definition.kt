package com.example.dictionary.domain.model.definition

import android.util.Log
import com.example.dictionary.util.TAG

// definition business model
data class Definition(

    val word: String,
    val score: Int,
    val numSyllables: Int,
    val tags: List<String>,
    val defs: List<String>?,
){
    var nouns: MutableList<String>? = mutableListOf()
    var verbs: MutableList<String>? = mutableListOf()
    var adjectives: MutableList<String>? = mutableListOf()
    var adverbs: MutableList<String>? = mutableListOf()

    init {
        setLists()
    }

    private fun setLists(){
        defs?.forEach {

            var type: String = ""
            var def: String = ""

            for (i in it.indices){
                if(it[i].compareTo('\t') == 0){
                    type = it.substring(0, i)
                    def = it.substringAfter("\t")
                }
            }

            if(type.compareTo("n") == 0){
                nouns?.add(def)
            }
            if(type.compareTo("v") == 0){
                verbs?.add(def)
            }
            if(type.compareTo("adj") == 0){
                adjectives?.add(def)
            }
            if(type.compareTo("adv") == 0){
                adverbs?.add(def)
            }
        }
    }
}