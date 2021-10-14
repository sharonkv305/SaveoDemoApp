package com.saveo.demo.handler

import android.widget.ImageView
import com.saveo.demo.model.Movie

interface MovieItemClickListener {

    fun onMovieClicked(movie: Movie?, imageView: ImageView)
}