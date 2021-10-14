package com.saveo.demo.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.saveo.demo.model.Movie
import com.saveo.demo.network.ApiInterface
import com.saveo.demo.utils.Constants
import java.lang.Exception

class MoviePagingSource(private val apiService: ApiInterface) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: FIRST_PAGE_INDEX
            Log.d("TAG", "page:${params.key}}")
            val response = apiService.getMoviesList(Constants.API_KEY, page)
            val nextPageNumber = response.page+1
            Log.d("TAG", "nextPageNumber: $nextPageNumber")
            LoadResult.Page(data = response.results, prevKey = null, nextKey = nextPageNumber)

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val FIRST_PAGE_INDEX = 1
    }
}