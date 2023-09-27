package com.github.ferumbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StartCookingDishResponse(

    @SerialName("dish_identifier")
    val dishIdentifier: Int
)
