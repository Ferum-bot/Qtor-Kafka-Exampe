package com.github.ferumbot.kafka

class ConsumerProperties {

    lateinit var dishTopic: String

    lateinit var bootstrapServer: String

    lateinit var consumerGroup: String

    lateinit var autoOffsetResetConfig: String
}