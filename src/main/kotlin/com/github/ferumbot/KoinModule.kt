package com.github.ferumbot

import com.github.ferumbot.components.DishCook
import com.github.ferumbot.components.DishCookConsumer
import com.github.ferumbot.components.DishCookProducer
import com.github.ferumbot.components.impl.DelayDishCook
import com.github.ferumbot.components.impl.KafkaDishCookConsumer
import com.github.ferumbot.components.impl.KafkaDishCookProducer
import com.github.ferumbot.repository.DishStorage
import com.github.ferumbot.repository.impl.InMemoryDishStorage
import com.github.ferumbot.services.CookExecutorService
import com.github.ferumbot.services.KitchenService
import com.github.ferumbot.services.impl.AsyncKitchenService
import com.github.ferumbot.services.impl.ThreadPoolCookExecutorService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val kafkaModule = module {

}

val kitchenModule = module {
    singleOf(::AsyncKitchenService) bind KitchenService::class
    singleOf(::ThreadPoolCookExecutorService) bind CookExecutorService::class
    singleOf(::InMemoryDishStorage) bind DishStorage::class
    singleOf(::DelayDishCook) bind DishCook::class
    singleOf(::KafkaDishCookConsumer) bind DishCookConsumer::class
    singleOf(::KafkaDishCookProducer) bind DishCookProducer::class
}