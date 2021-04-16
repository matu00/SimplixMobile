package com.example.visionmobile

import com.example.visionmobile.model.ArticleModel
import com.example.visionmobile.model.ProviderModel
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private val baseUrl = "http://vision-api-v1.herokuapp.com/api/"

    //Articles

    fun getArticles() : Call<List<ArticleModel>>{
        val retrofit = getRetrofit()
        return retrofit.getArticles()
    }

    fun getArticleById(id: Int) : Call<ArticleModel>{
        val retrofit = getRetrofit()
        return retrofit.getArticleById(id)
    }

    fun createArticle(article: ArticleModel): Call<ArticleModel>{
        val retrofit = getRetrofit()
        return retrofit.createArticle(article)
    }

    fun getArticleByCodeBar(article: ArticleModel) : Call<ArticleModel>{
        val retrofit = getRetrofit()
        return retrofit.getArticleByCodeBar(article)
    }

    fun searchArticle(keySearch: String): Call<List<ArticleModel>>{
        val retrofit = getRetrofit()
        return retrofit.searchArticle(keySearch)
    }

    //Providers

    fun getProviders() : Call<List<ProviderModel>>{
        val retrofit = getRetrofit()
        return retrofit.getProviders()
    }

    fun getProviderById(id: Int) : Call<ProviderModel>{
        val retrofit = getRetrofit()
        return retrofit.getProviderById(id)
    }

    fun searchProvider(keySearch: String): Call<List<ProviderModel>>{
        val retrofit = getRetrofit()
        return retrofit.searchProvider(keySearch)
    }

    private fun getRetrofit() : IAPIClient {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiClient: IAPIClient = retrofit.create(IAPIClient::class.java)

        return apiClient
    }
}