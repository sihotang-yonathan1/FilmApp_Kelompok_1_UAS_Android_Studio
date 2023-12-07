package com.example.filmapp.layout_configuration.reviewMovie

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.filmapp.R
import com.example.filmapp.api.model.MovieCastInfo
import com.example.filmapp.api.model.MovieReviewResult

class ReviewMovieViewHolder(inflater: LayoutInflater, parent: ViewGroup, val listener: ReviewMovieRecycleViewClickListener):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.review_item_template, parent, false)), OnClickListener{

    private var authorImageView: ImageView? = null
    private var authorNameTextView: TextView? = null
    private var reviewContentTextView: TextView? = null

    init {
        authorNameTextView = itemView.findViewById(R.id.film_review_author_name_text_view)
        authorImageView = itemView.findViewById(R.id.film_review_author_image)
        reviewContentTextView = itemView.findViewById(R.id.film_review_author_review_content_text_view)
        itemView.setOnClickListener(this)
    }

    fun bind (data: MovieReviewResult){
        authorNameTextView?.text = data.authorName

        if (data.authorDetails.avatarPath != null) {
            authorImageView?.load("https://image.tmdb.org/t/p/w185${data.authorDetails.avatarPath}")
        }
        else {
            authorImageView?.load("https://media.istockphoto.com/id/1409329028/vector/no-picture-available-placeholder-thumbnail-icon-illustration-design.jpg?s=612x612&w=0&k=20&c=_zOuJu755g2eEUioiOUdz_mHKJQJn-tDgIAhQzyeKUQ=")
        }
        reviewContentTextView?.text = data.content
    }

    override fun onClick(v: View?) {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION){
            listener.onReviewItemClicked(position)
        }
    }
}