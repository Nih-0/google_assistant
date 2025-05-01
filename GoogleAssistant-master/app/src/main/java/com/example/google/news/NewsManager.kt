
package com.example.google.news

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class NewsManager(private val context: Context) {
    fun fetchNews(callback: (List<NewsItem>) -> Unit) {
        val url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=7508077a51dc468d9c8f7b76bc76451d"
        val queue = Volley.newRequestQueue(context)
        
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val newsItems = parseNewsResponse(response)
                callback(newsItems)
            },
            { error ->
                callback(emptyList())
            }
        )
        queue.add(jsonObjectRequest)
    }

    private fun parseNewsResponse(response: JSONObject): List<NewsItem> {
        val newsItems = mutableListOf<NewsItem>()
        val articles = response.getJSONArray("articles")
        
        for (i in 0 until articles.length()) {
            val article = articles.getJSONObject(i)
            newsItems.add(
                NewsItem(
                    title = article.getString("title"),
                    description = article.getString("description")
                )
            )
        }
        return newsItems
    }
}

data class NewsItem(
    val title: String,
    val description: String
)
