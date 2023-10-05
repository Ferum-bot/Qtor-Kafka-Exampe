package com.github.ferumbot.services.impl

import com.github.ferumbot.model.Dish
import com.github.ferumbot.services.KitchenService
import com.github.ferumbot.services.model.DishFilter
import com.github.ferumbot.services.model.DishId
import com.github.ferumbot.services.model.DishOrder
import com.github.ferumbot.services.model.DishPayload

class AsyncKitchenService: KitchenService {

    override fun getDishById(id: DishId): Dish? {
        TODO("Not yet implemented")
    }

    override fun getAllDishes(filter: DishFilter, order: DishOrder): Collection<Dish> {
        TODO("Not yet implemented")
    }

    override fun startCookDish(dishPayload: DishPayload): DishId {
        TODO("Not yet implemented")
    }
}