package com.github.ferumbot.model

import kotlinx.datetime.Instant

data class Dish(
    val identifier: Int,
    val name: String,
    val status: DishStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
)
