package com.example.visionmobile

import com.example.visionmobile.model.ArticleModel
import com.example.visionmobile.model.ProviderModel
import retrofit2.Call
import retrofit2.http.*

interface IAPIClient {
    @GET("articles")
    fun getArticles(): Call<List<ArticleModel>>
    @GET("article/{id}")
    fun getArticleById(@Path("id") id: Int): Call<ArticleModel>
    @Headers("Content-Type: application/json")
    @POST("article")
    fun createArticle(@Body article: ArticleModel): Call<ArticleModel>
    @POST("article/scan")
    fun getArticleByCodeBar(@Body article: ArticleModel): Call<ArticleModel>
    @GET("article/search/{keySearch}")
    fun searchArticle(@Path("keySearch") keySearch: String): Call<List<ArticleModel>>

    //Providers
    @GET("providers")
    fun getProviders(): Call<List<ProviderModel>>
    @GET("provider/{id}")
    fun getProviderById(@Path("id") id: Int): Call<ProviderModel>
    @GET("provider/search/{keySearch}")
    fun searchProvider(@Path("keySearch") keySearch: String): Call<List<ProviderModel>>

}