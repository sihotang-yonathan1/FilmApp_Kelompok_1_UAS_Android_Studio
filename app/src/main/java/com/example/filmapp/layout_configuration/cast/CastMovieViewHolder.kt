package com.example.filmapp.layout_configuration.cast

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.filmapp.R
import com.example.filmapp.api.MovieResult
import com.example.filmapp.api.model.MovieCastInfo

class CastMovieViewHolder(inflater: LayoutInflater, parent: ViewGroup, val listener: CastMovieRecycleViewClickListener):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.cast_item_template, parent, false)), OnClickListener{

    private var castImageView: ImageView? = null
    private var castName: TextView? = null
    private var castCharacterName: TextView? = null

    init {
        castImageView = itemView.findViewById(R.id.film_detail_cast_image_view)
        castName = itemView.findViewById(R.id.film_detail_cast_name_text_view)
        castCharacterName = itemView.findViewById(R.id.film_detail_cast_character_name_text_view)

        itemView.setOnClickListener(this)
    }

    fun bind (data: MovieCastInfo){
        castName?.text = data.name
        if (data.profileImagePath != null) {
            castImageView?.load("https://image.tmdb.org/t/p/w185${data.profileImagePath}")
        }
        castCharacterName?.text = data.characterName

    }

    override fun onClick(v: View?) {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION){
            listener.onCastItemClicked(position)
        }
    }
}