package com.github.ferumbot.components

import com.github.ferumbot.model.Dish

interface DishCookConsumer {

    fun consumeCookTasks(): Collection<Dish>
}