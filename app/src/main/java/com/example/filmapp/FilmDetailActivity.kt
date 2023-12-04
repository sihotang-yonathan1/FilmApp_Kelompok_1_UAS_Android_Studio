package com.example.filmapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class FilmDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)

        val intent: Intent = intent
        val film_detail_title_label_text: String =
            intent.getIntExtra("com.example.filmapp.FILM_ID_EXTRAS", 0).toString()

        val film_detail_title_label_text_view: TextView = findViewById(R.id.fim_detail_title_label)
        film_detail_title_label_text_view.text = film_detail_title_label_text
    }
}