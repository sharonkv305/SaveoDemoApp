package com.saveo.demo.ui.adapter


import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.saveo.demo.R
import com.saveo.demo.handler.MovieItemClickListener
import com.saveo.demo.model.Movie
import com.saveo.demo.utils.Constants
import com.saveo.demo.utils.dpToPx


class HorizontalMovieListAdapter() :
    RecyclerView.Adapter<HorizontalMovieListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivMoviePoster)
        fun bind(movie: Movie) {
            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .error(R.drawable.placeholder_for_missing_posters)
            Glide.with(imageView).load(Constants.IMAGE_BASE_URL.plus(movie.backdropPath))

                .apply(options)
                .into(imageView)


        }
    }

    private val callBack = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id && oldItem.title == newItem.title
        }
    }

    val differ = AsyncListDiffer(this, callBack)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val displayWidth: Int = Resources.getSystem().displayMetrics.widthPixels
        holder.itemView.layoutParams.width = displayWidth - dpToPx(16) * 6
        differ.currentList[position].let { movie ->
            holder.bind(movie)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.horizontal_movie_item, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}