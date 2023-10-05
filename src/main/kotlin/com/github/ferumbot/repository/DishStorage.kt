package com.github.ferumbot.repository

import com.github.ferumbot.model.Dish
import com.github.ferumbot.model.DishStatus
import com.github.ferumbot.services.model.DishFilter
import com.github.ferumbot.services.model.DishOrder

interface DishStorage {

    fun getDishById(dishId: Long): Dish?

    fun getDishes(filter: DishFilter, order: DishOrder): Collection<Dish>

    fun updateDishStatus(dishId: Long, newStatus: DishStatus)

    fun saveDish(dish: Dish)
}