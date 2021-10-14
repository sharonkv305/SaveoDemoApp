package com.saveo.demo.ui.activity


import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.saveo.demo.R
import com.saveo.demo.databinding.ActivityMovieDetailsBinding
import com.saveo.demo.model.Genre
import com.saveo.demo.model.MovieDetailResponse
import com.saveo.demo.utils.Constants
import com.saveo.demo.utils.Status
import com.saveo.demo.viewmodel.MovieDetailsFragmentViewModel


class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    var movieId: Int? = null
    private val viewModel: MovieDetailsFragmentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBackButton.setOnClickListener { onBackPressed() }

        movieId = intent?.getIntExtra("id", 0)
        val imageUri = intent?.getStringExtra("image_uri")
        Glide.with(this)
            .load(Constants.IMAGE_BASE_URL.plus(imageUri))
            .into(binding.ivMoviePoster)

        viewModel.getMovieDetails(movieId)

        viewModel.movie.observe(this, {
            when (it) {

                is Status.Loading -> {
                }
                is Status.Success -> {
                    bindItems(it.data)
                }
                is Status.Error -> {
                    Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
                    Log.d("TAG", "Error: ${it.message}")
                }
            }
        })
    }

    private fun bindItems(data: MovieDetailResponse?) {
        data?.let {
            binding.tvMovieTittle.text = data.title
            binding.tvLanguage.text = data.originalLanguage
            binding.tvDuration.text = data.runtime.toString().plus(" min")
            binding.tvReleaseDate.text = data.releaseDate
            setCategoryChips(data.genres)
            binding.rating.rating = data.voteAverage.toFloat()
            binding.tvRating.text = data.voteAverage.toString()
            binding.tvOverView.text = data.overview
        }
    }

    private fun setCategoryChips(categories: List<Genre>) {
        for (category in categories) {
            val mChip =
                this.layoutInflater.inflate(R.layout.movie_gender_item, null, false) as Chip
            mChip.text = category.name
            /* val paddingDp = TypedValue.applyDimension(
                 TypedValue.COMPLEX_UNIT_DIP, 10f,
                 resources.displayMetrics
             ).toInt()
             mChip.setPadding(paddingDp, 0, paddingDp, 0)
             mChip.setOnCheckedChangeListener { compoundButton, b -> }*/
            binding.chipMovieGender.addView(mChip)
        }
    }
}