package com.github.ferumbot.services.impl

import com.github.ferumbot.components.DishCookProducer
import com.github.ferumbot.model.Dish
import com.github.ferumbot.model.DishStatus
import com.github.ferumbot.repository.DishStorage
import com.github.ferumbot.services.KitchenService
import com.github.ferumbot.services.model.DishFilter
import com.github.ferumbot.services.model.DishId
import com.github.ferumbot.services.model.DishOrder
import com.github.ferumbot.services.model.DishPayload
import kotlinx.datetime.Clock
import kotlin.random.Random

class AsyncKitchenService(
    private val storage: DishStorage,
    private val dishCookProducer: DishCookProducer,
): KitchenService {

    override fun getDishById(id: DishId): Dish? {
        return storage.getDishById(id.identifier)
    }

    override fun getAllDishes(filter: DishFilter, order: DishOrder): Collection<Dish> {
        return storage.getDishes(filter, order)
    }

    override fun startCookDish(dishPayload: DishPayload): DishId {
        val dish = Dish(
            identifier = Random.nextLong(),
            name = dishPayload.name,
            status = DishStatus.ACCEPTED,
            createdAt = Clock.System.now(),
            modifiedAt = Clock.System.now(),
        )

        dishCookProducer.sendCookTask(dish)
        storage.saveDish(dish)

        return DishId(dish.identifier)
    }
}