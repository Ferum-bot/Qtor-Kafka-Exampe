package com.github.ferumbot.services

import com.github.ferumbot.model.Dish
import com.github.ferumbot.services.model.DishFilter
import com.github.ferumbot.services.model.DishId
import com.github.ferumbot.services.model.DishOrder
import com.github.ferumbot.services.model.DishPayload

interface KitchenService {

    fun getDishById(id: DishId): Dish?

    fun getAllDishes(filter: DishFilter, order: DishOrder): Collection<Dish>

    fun startCookDish(dishPayload: DishPayload): DishId
}