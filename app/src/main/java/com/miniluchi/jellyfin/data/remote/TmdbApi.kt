package com.miniluchi.jellyfin.data.remote

import com.miniluchi.jellyfin.data.remote.dto.MovieDetailDto
import com.miniluchi.jellyfin.data.remote.dto.PopularMoviesResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "fr-FR",
        @Query("page") page: Int = 1,
    ): PopularMoviesResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "fr-FR",
    ): MovieDetailDto
}
