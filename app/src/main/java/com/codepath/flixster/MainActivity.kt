package com.example.flixsterplus

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "MainActivity"
private const val TRENDING_ACTORS_URL = "https://api.themoviedb.org/3/trending/person/week?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
private const val TRENDING_SHOWS_URL = "https://api.themoviedb.org/3/trending/tv/week?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"

class MainActivity : AppCompatActivity() {
    private val actors = mutableListOf<Actor>()
    private val shows = mutableListOf<TVShow>()
    private lateinit var actorsAdapter: ActorsAdapter
    private lateinit var showsAdapter: TVShowsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the RecyclerViews
        val rvActors = findViewById<RecyclerView>(R.id.rvActors)
        val rvShows = findViewById<RecyclerView>(R.id.rvShows)

        // Create adapters
        actorsAdapter = ActorsAdapter(this, actors)
        showsAdapter = TVShowsAdapter(this, shows)

        // Set adapters on RecyclerViews
        rvActors.adapter = actorsAdapter
        rvShows.adapter = showsAdapter

        // Fetch data from API
        fetchTrendingActors()
        fetchTrendingShows()
    }

    private fun fetchTrendingActors() {
        val client = AsyncHttpClient()
        client.get(TRENDING_ACTORS_URL, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d(TAG, "onSuccess: Actors JSON: $json")
                try {
                    val jsonArray = json.jsonObject.getJSONArray("results")
                    actors.addAll(Actor.fromJsonArray(jsonArray))
                    actorsAdapter.notifyDataSetChanged()
                    Log.i(TAG, "Actors: $actors")
                } catch (e: JSONException) {
                    Log.e(TAG, "Encountered exception: $e")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure: Failed to fetch trending actors $statusCode")
            }
        })
    }

    private fun fetchTrendingShows() {
        val client = AsyncHttpClient()
        client.get(TRENDING_SHOWS_URL, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d(TAG, "onSuccess: Shows JSON: $json")
                try {
                    val jsonArray = json.jsonObject.getJSONArray("results")
                    shows.addAll(TVShow.fromJsonArray(jsonArray))
                    showsAdapter.notifyDataSetChanged()
                    Log.i(TAG, "Shows: $shows")
                } catch (e: JSONException) {
                    Log.e(TAG, "Encountered exception: $e")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure: Failed to fetch trending shows $statusCode")
            }
        })
    }
}