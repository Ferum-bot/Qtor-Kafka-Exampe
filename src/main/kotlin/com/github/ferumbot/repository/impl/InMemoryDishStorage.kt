package com.github.ferumbot.repository.impl

import com.github.ferumbot.model.Dish
import com.github.ferumbot.model.DishStatus
import com.github.ferumbot.repository.DishStorage
import com.github.ferumbot.services.model.DishFilter
import com.github.ferumbot.services.model.DishOrder
import com.github.ferumbot.util.exceptions.KitchenDishException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class InMemoryDishStorage: DishStorage {

    private val storage: ConcurrentMap<Long, Dish> = ConcurrentHashMap()

    override fun getDishById(dishId: Long): Dish? {
        return storage[dishId]
    }

    override fun getDishes(filter: DishFilter, order: DishOrder): Collection<Dish> {
        val offset = filter.pageNumber * filter.pageSize
        val localCopyDishes = storage.values.toList()
        return localCopyDishes
            .distinctBy { it.identifier }
            .applyFilter(filter)
            .applyOrder(order)
            .take(offset, filter.pageSize)
    }

    override fun updateDishStatus(dishId: Long, newStatus: DishStatus) {
        storage.compute(dishId) { _, value ->
            if (value == null) {
                throw KitchenDishException("Dish with id $dishId not found!")
            }
            value.copy(status = newStatus)
        }
    }

    override fun saveDish(dish: Dish) {
        storage[dish.identifier] = dish
    }

    private fun Collection<Dish>.applyFilter(filter: DishFilter): Collection<Dish> {
        val filterFunction: (Dish) -> Boolean = when (filter) {
            is DishFilter.NoFilter -> {
                { true }
            }
            is DishFilter.IdFilter -> {
                { filter.ids.contains(it.identifier) }
            }
            is DishFilter.StatusFilter -> {
                { it.status == filter.status }
            }
        }
        return filter(filterFunction)
    }

    private fun Collection<Dish>.applyOrder(order: DishOrder): Collection<Dish> {
        val sortedDishes = sortedBy {
            return@sortedBy when (order) {
                is DishOrder.CreatedAtOrder -> it.createdAt
                is DishOrder.ModifiedAtOrder -> it.modifiedAt
            }
        }
        return if (order.isDesc) {
            sortedDishes.reversed()
        } else { sortedDishes }
    }

    private fun Collection<Dish>.take(offset: Int, size: Int): Collection<Dish> {
        return this.stream()
            .skip(offset.toLong())
            .limit(size.toLong())
            .toList()
    }
}