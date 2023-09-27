package com.github.ferumbot.model

enum class DishStatus(val alias: String, private val order: Int) {
    ACCEPTED("accepted", 0),
    INGREDIENTS_POLLING("ingredients_polling", 1),
    MIXING_INGREDIENTS("mixing_ingredients", 2),
    DISH_ROASTING("dish_roasting", 3),
    RETURN_PREPARING("return_preparing",4),
    COOKED("cooked", 5);

    fun next(): DishStatus {
        return when (order) {
            0 -> INGREDIENTS_POLLING
            1 -> MIXING_INGREDIENTS
            2 -> DISH_ROASTING
            3 -> RETURN_PREPARING
            4 -> COOKED
            else -> ACCEPTED
        }
    }
}