package com.github.ferumbot.components.impl

import com.github.ferumbot.components.DishCookConsumer
import com.github.ferumbot.kafka.ConsumerProperties
import com.github.ferumbot.model.Dish
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.koin.ext.getFullName
import java.time.Duration
import java.util.Properties

class KafkaDishCookConsumer(
    private val consumerProperties: ConsumerProperties,
): DishCookConsumer {

    companion object {

        private val logger = KotlinLogging.logger {  }

        private val json = Json {
            ignoreUnknownKeys = true
        }
    }

    private val kafkaConsumer: KafkaConsumer<String, String>

    init {
        val properties = Properties()
        properties.apply {
            setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.getFullName())
            setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.getFullName())
            setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProperties.bootstrapServer)
            setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.consumerGroup)
            setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerProperties.autoOffsetResetConfig)
        }

        kafkaConsumer = KafkaConsumer(properties)
        kafkaConsumer.subscribe(listOf(consumerProperties.dishTopic))
    }

    override fun consumeCookTasks(): Collection<Dish> {
        val records = kafkaConsumer.poll(Duration.ofMillis(100))
        logger.info { "Received ${records.count()} records from kafka" }
        val dishes = records.map { record ->
            val value = record.value()
            json.decodeFromString<Dish>(value)
        }
        return dishes
    }
}