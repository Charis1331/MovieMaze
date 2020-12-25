package com.haris.houlis.moviemaze.data.mapper

interface DataMapper<S, D> {

    fun toDomain(source: S): D
}