package com.saveo.demo.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.saveo.demo.R
import com.saveo.demo.handler.MovieItemClickListener
import com.saveo.demo.model.Movie
import com.saveo.demo.utils.Constants


class MovieListAdapter(var listener: MovieItemClickListener) :
    PagingDataAdapter<Movie, MovieListAdapter.ViewHolder>(DiffUtilCallBack()) {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.ivMoviePoster)


        fun bind(movie: Movie) {
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .error(R.drawable.placeholder_for_missing_posters)
            Glide.with(imageView).load(Constants.IMAGE_BASE_URL.plus(movie.posterPath))
                .apply(options)
                .into(imageView)


        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }

        holder.imageView.setOnClickListener {
            listener.onMovieClicked(getItem(position),holder.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id && oldItem.title == newItem.title
        }
    }
}