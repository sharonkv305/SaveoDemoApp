package com.saveo.demo.network

import com.saveo.demo.model.Movie
import com.saveo.demo.model.MovieDetailResponse
import com.saveo.demo.model.MovieResponse
import com.saveo.demo.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET(EndPoints.getMovieList)
    suspend fun getMoviesList(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): MovieResponse

    @GET(EndPoints.getMovieDetails)
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String

    ): MovieDetailResponse


    @GET(EndPoints.getTopRatedMovies)
    suspend fun topRatedMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int
    ): MovieResponse

}