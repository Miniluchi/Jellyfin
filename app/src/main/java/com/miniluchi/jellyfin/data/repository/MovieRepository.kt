package com.miniluchi.jellyfin.data.repository

import com.miniluchi.jellyfin.BuildConfig
import com.miniluchi.jellyfin.data.remote.RetrofitInstance
import com.miniluchi.jellyfin.data.remote.TmdbApi
import com.miniluchi.jellyfin.data.remote.dto.MovieDetailDto
import com.miniluchi.jellyfin.data.remote.dto.MovieDto
import com.miniluchi.jellyfin.domain.model.Movie
import com.miniluchi.jellyfin.domain.model.MovieDetail

class MovieRepository(
    private val api: TmdbApi = RetrofitInstance.api,
    private val apiKey: String = BuildConfig.TMDB_API_KEY,
) {

    suspend fun getPopularMovies(page: Int = 1): List<Movie> {
        ensureApiKey()
        return api.getPopularMovies(apiKey = apiKey, page = page)
            .results
            .map { it.toDomain() }
    }

    suspend fun getMovieDetail(movieId: Int): MovieDetail {
        ensureApiKey()
        return api.getMovieDetail(movieId = movieId, apiKey = apiKey).toDomain()
    }

    private fun ensureApiKey() {
        if (apiKey.isBlank()) {
            throw MissingApiKeyException()
        }
    }
}

class MissingApiKeyException : IllegalStateException("Missing TMDB API key")

private fun MovieDto.toDomain(): Movie = Movie(
    id = id,
    title = title.orEmpty(),
    overview = overview.orEmpty(),
    posterUrl = posterPath?.let { buildImageUrl("w500", it) },
    backdropUrl = backdropPath?.let { buildImageUrl("w780", it) },
    releaseDate = releaseDate.orEmpty(),
    voteAverage = voteAverage ?: 0.0,
)

private fun MovieDetailDto.toDomain(): MovieDetail = MovieDetail(
    id = id,
    title = title.orEmpty(),
    originalTitle = originalTitle.orEmpty(),
    overview = overview.orEmpty(),
    tagline = tagline.orEmpty(),
    posterUrl = posterPath?.let { buildImageUrl("w500", it) },
    backdropUrl = backdropPath?.let { buildImageUrl("w780", it) },
    releaseDate = releaseDate.orEmpty(),
    runtimeMinutes = runtime ?: 0,
    status = status.orEmpty(),
    homepage = homepage.orEmpty(),
    voteAverage = voteAverage ?: 0.0,
    voteCount = voteCount ?: 0,
    genres = genres?.map { it.name }.orEmpty(),
)

private fun buildImageUrl(size: String, path: String): String =
    "${BuildConfig.TMDB_IMAGE_BASE_URL}$size$path"
