package com.dcchua.gweather.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val firstName: String
)
