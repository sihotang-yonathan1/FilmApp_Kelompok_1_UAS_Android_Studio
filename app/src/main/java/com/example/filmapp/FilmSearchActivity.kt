package com.example.filmapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.GridView
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmapp.api.MovieResult
import com.example.filmapp.api.MovieResultPage
import com.example.filmapp.api.MovieSearchService
import com.example.filmapp.api.PopularFilmService
import com.example.filmapp.layout_configuration.filmSearch.FilmSearchMovieAdapter
import com.example.filmapp.layout_configuration.filmSearch.FilmSearchRecycleViewClickListener
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Array

class FilmSearchActivity : AppCompatActivity(), FilmSearchRecycleViewClickListener{
    val FILM_ID_EXTRAS = "com.example.filmapp.FILM_ID_EXTRAS"
    val SEARCH_QUERY = "com.example.filmapp.SEARCH_QUERY"
    var searchData = ArrayList<MovieResult>()
    var searchQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_search)

        val intent = intent
        val queryText: String = intent.getStringExtra(SEARCH_QUERY).toString()
        searchQuery = queryText

        val searchView: SearchView = findViewById(R.id.film_search_search_view)


        val searchRecyclerView: RecyclerView = findViewById(R.id.film_search_recycler_view)
        val searchRecyclerViewLayoutManager = GridLayoutManager(this, 2)
        searchRecyclerView.layoutManager = searchRecyclerViewLayoutManager

        fun setSearchAdapter(){
            val searchAdapter = FilmSearchMovieAdapter(data = searchData, this)
            searchRecyclerView.adapter = searchAdapter
        }

        fun updateState() {
            Log.d("updateState - searchQuery", "updateState: $searchQuery")
            runBlocking {
                val searchResultDeferred = async {
                    getSearchFilm(query = searchQuery!!)
                }

                val searchResultPage = searchResultDeferred.await()
                Log.d("updateState", "updateState: searchResultPage: $searchResultPage")
                if (searchResultPage != null) {
                    searchData = ArrayList(searchResultPage.results)
                    setSearchAdapter()
                }
            }
        }
        updateState()

        searchView.setOnQueryTextListener (object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    searchQuery = query
                    updateState()
                }
                return false
            }
        })

    }

    override fun onSearchItemClicked(position: Int) {
        Log.d("OnPopularItemClicked", "onPopularItemClicked: pos: $position")
        val filmId: Int = searchData[position].id
        val intent: Intent = Intent(this, FilmDetailActivity::class.java)
        intent.putExtra(FILM_ID_EXTRAS, filmId)
        startActivity(intent)
    }
}

suspend fun getSearchFilm(query: String, apiKey: String = "9296a7b78a765608a22b237fe8e1dc2e"): MovieResultPage? {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofitObj = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    try {
        val service = retrofitObj.create(MovieSearchService::class.java)
        return service.getMovieSearch(
            query = query,
            apiKey = apiKey
        )
    }
    catch (e: HttpException){
        Log.e(
            "HTTP_ERROR",
            "getPopularFilm: HTTP ${e.code()} ${e.message()}: ${e.response()?.errorBody()}", )
        return null
    }
}