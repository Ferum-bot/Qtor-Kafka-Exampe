package com.github.ferumbot.components

import com.github.ferumbot.model.Dish

interface DishCook {

    suspend fun cookDish(dish: Dish): Dish
}