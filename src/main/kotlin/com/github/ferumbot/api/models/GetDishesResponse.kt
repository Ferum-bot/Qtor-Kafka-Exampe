package com.github.ferumbot.api.models

import com.github.ferumbot.model.Dish
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetDishesResponse(

    @SerialName("dishes")
    val dishes: Collection<Dish>
)
