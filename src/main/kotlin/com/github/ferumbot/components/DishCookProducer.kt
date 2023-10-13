package com.github.ferumbot.components

import com.github.ferumbot.model.Dish

interface DishCookProducer {

    fun sendCookTask(dish: Dish)
}