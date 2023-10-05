package com.github.ferumbot.api

import com.github.ferumbot.api.models.GetDishResponse
import com.github.ferumbot.api.models.GetDishesRequest
import com.github.ferumbot.api.models.GetDishesResponse
import com.github.ferumbot.api.models.StartCookingDishRequest
import com.github.ferumbot.api.models.StartCookingDishResponse
import com.github.ferumbot.services.KitchenService
import com.github.ferumbot.services.model.DishFilter
import com.github.ferumbot.services.model.DishId
import com.github.ferumbot.services.model.DishOrder
import com.github.ferumbot.services.model.DishPayload
import com.github.ferumbot.util.exceptions.KitchenDishException
import com.github.ferumbot.util.extensions.pathParameter
import com.github.ferumbot.util.extensions.receive
import com.github.ferumbot.util.extensions.send
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.withKitchenApi() {
    routing {

        val kitchenService by inject<KitchenService>()

        get("/kitchen/{dishId}") {
            val dishIdentifier = pathParameter("dishId")?.toLong()
                ?: throw KitchenDishException("Invalid path parameter: dishId")

            val targetDish = kitchenService.getDishById(DishId(dishIdentifier))
            val response = GetDishResponse(targetDish)

            send(response)
        }

        get("/kitchen/dishes") {
            val request = receive<GetDishesRequest>()
            val filter = DishFilter.NoFilter(
                pageSize = request.pageSize,
                pageNumber = request.pageNumber,
            )
            val order = DishOrder.ModifiedAtOrder(isDesc = false)

            val resultDishes = kitchenService.getAllDishes(filter, order)
            val response = GetDishesResponse(dishes = resultDishes)

            send(response)
        }

        put("/kitchen/cooking/start") {
            val request = receive<StartCookingDishRequest>()
            val dishPayload = DishPayload(name = request.dishName)

            val resultDish = kitchenService.startCookDish(dishPayload)
            val response = StartCookingDishResponse(resultDish.identifier)

            send(response)
        }
    }
}