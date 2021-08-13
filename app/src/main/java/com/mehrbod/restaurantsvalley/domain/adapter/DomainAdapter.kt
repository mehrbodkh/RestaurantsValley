package com.mehrbod.restaurantsvalley.domain.adapter

interface DomainAdapter<T, DomainModel> {
    fun mapToDomainModel(model: T): DomainModel
    fun mapFromDomainModel(model: DomainModel): T
}