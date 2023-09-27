package com.github.ferumbot.components.impl

import com.github.ferumbot.components.DishCook
import com.github.ferumbot.model.Dish
import com.github.ferumbot.model.DishStatus
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock

class DelayDishCook : DishCook {

    companion object {

        private val logger = KotlinLogging.logger {  }
    }

    override suspend fun cookDish(dish: Dish): Dish {
        logger.info { "Received dish(${dish.identifier}) with name: ${dish.identifier} and status: ${dish.status.alias}" }

        if (dish.status == DishStatus.COOKED) {
            logger.warn { "Dish(${dish.identifier}) is already cooked!" }
            return dish
        }

        val cookTime = getRandomCookTime()
        logger.info { "Dish(${dish.identifier}) will be cooking for $cookTime millis" }
        delay(cookTime)
        logger.info { "Dish(${dish.identifier}) successfully cooked!" }

        return dish.copy(
            status = dish.status.next(),
            updatedAt = Clock.System.now(),
        )
    }

    private fun getRandomCookTime(): Long {
        val availableCookTime = listOf(
            25L, 200L, 350L, 750L, 1000L, 2000L, 6000L
        )
        val index = (0..availableCookTime.lastIndex).random()
        return availableCookTime[index]
    }
}