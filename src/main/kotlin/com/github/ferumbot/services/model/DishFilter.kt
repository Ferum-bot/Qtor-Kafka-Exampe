package com.github.ferumbot.services.model

sealed class DishFilter(open val pageNumber: Int, open val pageSize: Int) {

    object NoFilter : DishFilter(pageNumber = 0, pageSize = 30)

    data class CreatedAtOrder(
        override val pageSize: Int,
        override val pageNumber: Int,
        val isDesc: Boolean = false,
    ) : DishFilter(pageNumber, pageSize)

    data class ModifiedAtOrder(
        override val pageSize: Int,
        override val pageNumber: Int,
        val isDesc: Boolean = false,
    ) : DishFilter(pageNumber, pageSize)
}
