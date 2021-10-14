package com.saveo.demo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.saveo.demo.model.Movie
import com.saveo.demo.model.MovieResponse
import com.saveo.demo.network.ApiInterface
import com.saveo.demo.network.RetroInstance
import com.saveo.demo.paging.MoviePagingSource
import com.saveo.demo.utils.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PopularMoviesViewModel : ViewModel() {

    var apiInterface: ApiInterface =
        RetroInstance.getRetroInstance().create(ApiInterface::class.java)

    fun getMovieList(): Flow<PagingData<Movie>> {
        return Pager(config = PagingConfig(pageSize = 1),
            pagingSourceFactory = { MoviePagingSource(apiInterface) }).flow.cachedIn(viewModelScope)
    }

    suspend fun topRatedMovies(): Flow<Status<MovieResponse>> = flow {
        try {
            val response = apiInterface.topRatedMovies(page = 1)
            emit(Status.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}