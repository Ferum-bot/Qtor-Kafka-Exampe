package com.github.ferumbot.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetDishesRequest(

    @SerialName("page_number")
    val pageNumber: Int,

    @SerialName("page_size")
    val pageSize: Int,
)
