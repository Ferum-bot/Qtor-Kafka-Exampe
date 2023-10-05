package com.github.ferumbot.services.model

import com.github.ferumbot.model.DishStatus

sealed class DishFilter(open val pageNumber: Int, open val pageSize: Int) {

    data class NoFilter(
        override val pageNumber: Int,
        override val pageSize: Int,
    ) : DishFilter(pageNumber, pageSize)

    data class IdFilter(
        val ids: Collection<Long>,
        override val pageNumber: Int,
        override val pageSize: Int,
    ) : DishFilter(pageNumber, pageSize)

    data class StatusFilter(
        val status: DishStatus,
        override val pageNumber: Int,
        override val pageSize: Int,
    ) : DishFilter(pageNumber, pageSize)
}
