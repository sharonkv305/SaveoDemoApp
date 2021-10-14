package com.saveo.demo.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.saveo.demo.databinding.ActivityPopularMoviesBinding
import com.saveo.demo.handler.MovieItemClickListener
import com.saveo.demo.model.Movie
import com.saveo.demo.ui.adapter.HorizontalMovieListAdapter
import com.saveo.demo.ui.adapter.MovieListAdapter
import com.saveo.demo.utils.Status
import com.saveo.demo.viewmodel.PopularMoviesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs


class PopularMoviesActivity : AppCompatActivity(), MovieItemClickListener {

    private lateinit var binding: ActivityPopularMoviesBinding
    private lateinit var movieListAdapter: MovieListAdapter
    private val viewModel: PopularMoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopularMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                //  Collapsed
                binding.tvTitle.text = "Now Playing"
            } else {
                //Expanded
                binding.tvTitle.text = "Movies"
            }
        })

        binding.rvMovieList.apply {
            movieListAdapter = MovieListAdapter(this@PopularMoviesActivity)
            adapter = movieListAdapter
        }

        val horizontalMovieListAdapter = HorizontalMovieListAdapter()
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvHorizontalMovieList.apply {
            layoutManager = manager
            adapter = horizontalMovieListAdapter
        }
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvHorizontalMovieList)

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getMovieList().collectLatest {

                movieListAdapter.submitData(it)
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.topRatedMovies().collect {
                when (it) {
                    is Status.Loading -> {
                    }
                    is Status.Success -> {
                        binding.shimmerTopRatedMovieList.stopShimmer()
                        binding.shimmerTopRatedMovieList.isVisible = false
                        binding.rvHorizontalMovieList.isVisible = true
                        horizontalMovieListAdapter.differ.submitList(it.data?.results)
                    }
                    is Status.Error -> {
                        Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG)
                            .show()

                    }
                }

            }
        }
    }

    override fun onMovieClicked(movie: Movie?, imageView: ImageView) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("id", movie?.id)
        intent.putExtra("image_uri", movie?.posterPath)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            imageView,
            "image"
        )
        startActivity(intent, options.toBundle())
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerTopRatedMovieList.startShimmer()
    }

    override fun onPause() {
        binding.shimmerTopRatedMovieList.stopShimmer()
        super.onPause()
    }
}