package com.saveo.demo.viewmodel

import android.provider.SyncStateContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveo.demo.model.Movie
import com.saveo.demo.model.MovieDetailResponse
import com.saveo.demo.network.ApiInterface
import com.saveo.demo.network.RetroInstance
import com.saveo.demo.utils.Constants
import com.saveo.demo.utils.Status
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsFragmentViewModel : ViewModel() {
    private val apiInterface: ApiInterface =
        RetroInstance.getRetroInstance().create(ApiInterface::class.java)
    val movie = MutableLiveData<Status<MovieDetailResponse>>()


    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        movie.postValue(Status.Error(throwable.message))
    }

    fun getMovieDetails(movieId: Int?) {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            movie.postValue(Status.Loading())
            val movieDetail = apiInterface.getMovieDetails(movieId, Constants.API_KEY)
            movie.postValue(Status.Success(movieDetail))
        }
    }
}