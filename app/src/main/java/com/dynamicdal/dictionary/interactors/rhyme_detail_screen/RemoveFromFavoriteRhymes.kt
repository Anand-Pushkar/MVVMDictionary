package com.dynamicdal.dictionary.interactors.rhyme_detail_screen

import android.util.Log
import com.dynamicdal.dictionary.cache.rhyme.RhymeDao
import com.dynamicdal.dictionary.cache.rhyme.entities.RhymeEntity
import com.dynamicdal.dictionary.cache.rhyme.mapper.RhymeEntityMapper
import com.dynamicdal.dictionary.cache.rhyme.response.RhymeWithAllTheWordsResponse
import com.dynamicdal.dictionary.domain.data.DataState
import com.dynamicdal.dictionary.domain.model.rhyme.Rhyme
import com.dynamicdal.dictionary.domain.model.rhyme.Rhymes
import com.dynamicdal.dictionary.network.WordService
import com.dynamicdal.dictionary.network.rhyme.model.RhymeDtoMapper
import com.dynamicdal.dictionary.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@ExperimentalStdlibApi
class RemoveFromFavoriteRhymes(
    val dtoMapper: RhymeDtoMapper,
    val wordService: WordService,
    val entityMapper: RhymeEntityMapper,
    val rhymeDao: RhymeDao
) {

    fun execute(
        rhymes: Rhymes,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<Rhymes>> = flow {

        try {
            // loading
            emit(DataState.loading<Rhymes>())

            // remove from cache
            removeRhyme(rhymes)

            // check if cache still holds the values
            val cacheResult = getFavRhymeRhymeFromCache(rhymes)

            // if cache still holds the rhyme, delete again, although this should not happen
            if (cacheResult != null) {
                removeRhyme(rhymes)
            }

            // get the rhymes from network and emit
            if (isNetworkAvailable) {
                val query = rhymes.mainWord
                val networkResult = getRhymesFromNetwork(query)
                emit(DataState.success(
                    Rhymes(
                        mainWord = query,
                        rhymeList = networkResult.sortedBy { it.numSyllables }
                    )
                ))
            } else {
                rhymes.isFavorite = false
                emit(DataState.success(rhymes))
            }

        } catch (e: Exception) {
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<Rhymes>(e.message ?: "unknown error"))
        }
    }

    private suspend fun getFavRhymeRhymeFromCache(rhymes: Rhymes): Rhymes? {
        return rhymeDao.getWordWithAllTheRhymes(rhymes.mainWord)?.let {
            entityMapper.mapToRhymesModel(it)
        }
    }

    private suspend fun getRhymesFromNetwork(sQuery: String): List<Rhyme> {
        return dtoMapper.toDomainList(
            wordService.getRhymes(
                searchQuery = sQuery
            )
        )
    }

    private suspend fun removeRhyme(rhymes: Rhymes) {
        // step 1 remove from myRhymes table
        rhymeDao.deleteMyRhyme(entityMapper.mapFromRhymesModel(rhymes))
        // step 2 remove all the relations from cross ref table
        rhymeDao.deleteCrossRefByMWord(rhymes.mainWord)
        // step 3 get List<RhymeWithAllTheWordsResponse>
        val rhymesWithRelation = rhymeDao.getAllRhymesWithTheirWords()
        // step 4 get List<RhymeEntity>
        val allRhymes = rhymeDao.getAllRhymes()
        // step 5 get a list of  all the rhymes that only exist in List<RhymeEntity> and not in List<RhymeWithAllTheWordsResponse>
        val orphanedRhymes = getOrphanedRhymes(rhymesWithRelation, allRhymes)
        // step 6 delete the orphaned rhymes from the rhymes table
        rhymeDao.deleteManyRhymes(orphanedRhymes)
    }

    private fun getOrphanedRhymes(
        rhymesWithRelation: List<RhymeWithAllTheWordsResponse>,
        allRhymes: List<RhymeEntity>
    ): List<RhymeEntity> {

        // extract List<RhymeEntity> from List<RhymeWithAllTheWordsResponse>
        val rlRhymes = mutableListOf<RhymeEntity>()
        rhymesWithRelation.forEach {
            rlRhymes.add(it.rhyme)
        }
        return (allRhymes.toSet() - rlRhymes.toSet()).toList()
    }
}