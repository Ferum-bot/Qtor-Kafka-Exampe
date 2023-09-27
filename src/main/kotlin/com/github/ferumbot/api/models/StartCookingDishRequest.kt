package com.github.ferumbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartCookingDishRequest(

    @SerialName("dish_name")
    val dishName: String
)
