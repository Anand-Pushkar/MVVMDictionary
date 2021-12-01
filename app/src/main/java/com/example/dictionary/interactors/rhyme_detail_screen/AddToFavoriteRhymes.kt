package com.example.dictionary.interactors.rhyme_detail_screen

import android.util.Log
import com.example.dictionary.cache.rhyme.RhymeDao
import com.example.dictionary.cache.rhyme.mapper.RhymeEntityMapper
import com.example.dictionary.domain.data.DataState
import com.example.dictionary.domain.model.rhyme.Rhymes
import com.example.dictionary.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@ExperimentalStdlibApi
class AddToFavoriteRhymes (
    val entityMapper: RhymeEntityMapper,
    val rhymeDao: RhymeDao
) {
    fun execute(
        rhymes: Rhymes
    ): Flow<DataState<Rhymes>> = flow {

        try {
            // loading
            emit(DataState.loading<Rhymes>())

            // insert rhyme into cache
            insertRhyme(rhymes)

            // read from the cache
            val favRhyme = getFavRhymeRhymeFromCache(rhymes)

            // if cache holds the rhyme
            if(favRhyme != null){
                emit(DataState.success(
                    Rhymes(
                        mainWord = favRhyme.mainWord,
                        rhymeList = favRhyme.rhymeList?.sortedBy { it.numSyllables },
                        isFavorite = favRhyme.isFavorite
                    )
                ))
            } else{
                // should not be null now but if it happens throw exception
                throw Exception("Unable to get the rhyme from the cache.")
            }

        }catch (e: Exception){
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<Rhymes>(e.message?: "unknown error"))
        }

    }

    private suspend fun getFavRhymeRhymeFromCache(rhymes: Rhymes): Rhymes? {
        return rhymeDao.getWordWithAllTheRhymes(rhymes.mainWord)?.let {
            entityMapper.mapToRhymesModel(it)
        }
    }

    private suspend fun insertRhyme(rhymes: Rhymes){
        // step 1 insert into myRhymes table
        rhymeDao.insertMyRhyme(entityMapper.mapFromRhymesModel(rhymes))

        // step 2 insert list of rhymes into rhymes table
        rhymes.rhymeList?.let { entityMapper.fromDomainList(it) }?.let { rhymeDao.insertRhymes(it) }

        // step 3 insert every relation in cross ref table
        rhymes.rhymeList?.forEach { rhyme ->
            rhymeDao.insertRhymeZoneCrossRef(entityMapper.mapToRhymeZoneCrossRef(rhymes.mainWord, rhyme.word))
        }
    }
}