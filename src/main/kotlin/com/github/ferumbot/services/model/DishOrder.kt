package com.github.ferumbot.services.model

sealed class DishOrder(
    open val isDesc: Boolean
) {
    data class CreatedAtOrder(
        override val isDesc: Boolean
    ) : DishOrder(isDesc)

    data class ModifiedAtOrder(
        override val isDesc: Boolean
    ) : DishOrder(isDesc)
}
