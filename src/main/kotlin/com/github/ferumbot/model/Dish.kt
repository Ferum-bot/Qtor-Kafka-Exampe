package com.github.ferumbot.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Dish(

    @SerialName("identifier")
    val identifier: Long,

    @SerialName("name")
    val name: String,

    @SerialName("dish_status")
    val status: DishStatus,

    @SerialName("created_at")
    val createdAt: Instant,

    @SerialName("modified_at")
    val modifiedAt: Instant,
)
