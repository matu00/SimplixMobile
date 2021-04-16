package com.example.visionmobile.model

import java.io.Serializable

data class ArticleModel (
        val id: Int?,
        val line: String?,
        val code: String?,
        val product: String?,
        val description: String?,
        val price: String?,
        val coefficient: String?,
        val provider_id: String?,
        val provider: ProviderModel?,
        val created_at: String?,
        val updated_at: String?,
        val codebar: String?
): Serializable
