package com.github.ferumbot.components.impl

import com.github.ferumbot.components.DishCookProducer
import com.github.ferumbot.kafka.ProducerProperties
import com.github.ferumbot.model.Dish
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.Properties

class KafkaDishCookProducer(
    private val producerProperties: ProducerProperties,
): DishCookProducer {

    companion object {

        private val json = Json { ignoreUnknownKeys = true }
    }

    private val kafkaProducer: KafkaProducer<String, String>

    init {
        val properties = Properties()

        kafkaProducer = KafkaProducer(properties)
    }

    override fun sendCookTask(dish: Dish) {
        val stringDish = json.encodeToString(dish)
        val recordDish = ProducerRecord(
            producerProperties.topic, dish.identifier.toString(), stringDish
        )

        kafkaProducer.send(recordDish)
    }
}