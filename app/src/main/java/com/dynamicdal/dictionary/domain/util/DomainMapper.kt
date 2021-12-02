package com.dynamicdal.dictionary.domain.util

/**
 * This interface holds the functions to map the domain
 * models Definition.kt and Rhymes.kt to and from our
 * Entity (Cache model), and Dto (Network model).
 *
 * We will implement this interface in our DtoMapper and EntityMapper.
 */

interface DomainMapper <T, DomainModel>{

    fun mapToDomainModel(model: T): DomainModel

    fun mapFromDomainModel(domainModel: DomainModel) : T
}