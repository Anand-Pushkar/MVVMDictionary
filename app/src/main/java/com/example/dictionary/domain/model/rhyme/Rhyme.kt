package com.example.dictionary.domain.model.rhyme

// rhyme business model

@ExperimentalStdlibApi
data class Rhymes(
    val mainWord: String,
    val rhymeList: List<Rhyme>?,
    var isFavorite: Boolean = false // default value is false and will only change when the word becomes favorite and is inserted into cache
) {
    var rhymesMap: Map<String, List<Rhyme>>? = null
    var syllableInfo: String = ""

    init {
        rhymesMap = getRhymesMap(rhymeList)
        syllableInfo = getSyllableInfo(rhymesMap)
    }

    private fun getSyllableInfo(rhymesMap: Map<String, List<Rhyme>>?): String {

        rhymesMap?.let { map ->
            var str = ""
            val mapSize = map.size
            var iterator = 0

            map.forEach { (numSyllable, rhymes) ->

                if (iterator == mapSize - 2) {
                    str += "${numSyllable} and "
                } else if (iterator == mapSize - 1) {
                    str += "${numSyllable} syllable rhymes"
                } else {
                    str += "${numSyllable}, "
                }
                iterator += 1
            }
            return str
        }
        return ""
    }

    @ExperimentalStdlibApi
    private fun getRhymesMap(rhymes: List<Rhyme>?): Map<String, List<Rhyme>>? {
        rhymes?.let { rhymeList ->
            return buildMap<String, List<Rhyme>> {
                var lowerIndex = 0
                for (i in 0..(rhymeList.size - 1)) {

                    if ((i != 0 && rhymeList[i].numSyllables > rhymeList[i - 1].numSyllables) || i == rhymeList.size - 1) {

                        if (i == rhymeList.size - 1) {
                            this["${rhymeList[i].numSyllables}"] =
                                rhymeList.subList(lowerIndex, i + 1)
                        } else {
                            this["${rhymeList[i - 1].numSyllables}"] =
                                rhymeList.subList(lowerIndex, i)
                            lowerIndex = i
                        }
                    }
                }
            }
        }
        return null
    }
}

data class Rhyme(
    val word: String,
    val score: Int,
    val numSyllables: Int,
)

data class RhymesMinimal(
    val word: String,
    val syllableInfo: String
)