package com.example.visionmobile.model

import java.io.Serializable
import java.math.BigInteger

data class ProviderModel (
        val id: Int?,
        val businessname: String?,
        val cuit: BigInteger?,
        val address: String?,
        val province: String?,
        val location: String?,
        val email: String?,
        val note: String?,
        val responsibility_id: Int?,
        val created_at: String?,
        val updated_at: String?
): Serializable