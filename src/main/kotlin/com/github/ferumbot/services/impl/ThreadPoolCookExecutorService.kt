package com.github.ferumbot.services.impl

import com.github.ferumbot.components.DishCook
import com.github.ferumbot.components.DishCookConsumer
import com.github.ferumbot.model.Dish
import com.github.ferumbot.repository.DishStorage
import com.github.ferumbot.services.CookExecutorService
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

class ThreadPoolCookExecutorService(
    private val consumer: DishCookConsumer,
    private val dishCook: DishCook,
    private val storage: DishStorage,
) : CookExecutorService {

    private val thread = Thread(ThreadGroup("KafkaConsumerGroup"), this::threadLogic)

    private val cookScope = CoroutineScope(Dispatchers.IO)

    override fun startExecution() {
        thread.start()
    }

    override fun terminateExecution() {
        thread.interrupt()
        thread.join()
    }

    private fun threadLogic() {
        while (!Thread.interrupted()) {
            val supervisorJob = SupervisorJob()
            val dishesToCook = consumer.consumeCookTasks()
            val cookJobs = mutableListOf<Deferred<Dish>>()

            dishesToCook.forEach { dish ->
                val coroutineName = CoroutineName("DishCoroutine(${dish.identifier})")
                val job = cookScope.async(coroutineName + supervisorJob) {
                    dishCook.cookDish(dish)
                }
                cookJobs.add(job)
            }

            val cockedDishes = runBlocking { cookJobs.awaitAll() }
            cockedDishes.forEach { dish ->
                storage.updateDishStatus(dish.identifier, dish.status)
            }
        }
    }
}