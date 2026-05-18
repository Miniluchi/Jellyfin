package com.miniluchi.jellyfin.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String,
    val voteAverage: Double,
)

data class MovieDetail(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val tagline: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String,
    val runtimeMinutes: Int,
    val status: String,
    val homepage: String,
    val voteAverage: Double,
    val voteCount: Int,
    val genres: List<String>,
)
