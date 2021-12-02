package com.dynamicdal.dictionary.domain.model.definition


// definition business model
data class Definition(

    // values from api
    val word: String,
    val score: Int,
    val numSyllables: Int,
    val tags: List<String>,
    val defs: List<String>?,

    // generated/modeled values
    var isFavorite: Boolean = false // default value is false and will only change when the word becomes favorite and is inserted into cache
){
    // generated/modeled values
    var nouns: MutableList<String>? = mutableListOf()
    var verbs: MutableList<String>? = mutableListOf()
    var adjectives: MutableList<String>? = mutableListOf()
    var adverbs: MutableList<String>? = mutableListOf()
    var statement: String = ""
    val pronunciation = tags[1].substringAfter(":")


    init {
        setLists()
    }

    private fun setLists(){


        defs?.forEachIndexed { index, it ->

            var type: String = ""
            var def: String = ""

            for (i in it.indices){ // traversing a definition
                if(it[i].compareTo('\t') == 0){
                    type = it.substring(0, i)
                    def = it.substringAfter("\t")
                }
                if(index == 0){
                    statement = def
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

data class DefinitionMinimal(
    val word: String,
    val pronunciation: String,
    val statement: String
)