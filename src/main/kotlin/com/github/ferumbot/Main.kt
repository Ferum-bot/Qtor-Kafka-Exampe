package com.github.ferumbot

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.netty4.NettyEventExecutorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import kotlinx.serialization.json.Json
import org.koin.core.logger.Level
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) {
    embeddedServer(
        factory = Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::kitchenModule,
        configure = NettyApplicationEngine.Configuration::configureServer
    ).start(wait = true)
}

private fun Application.kitchenModule() {
    install(MicrometerMetrics) {
        registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
        meterBinders = listOf(
            JvmThreadMetrics(),
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            ProcessorMetrics()
        )
    }
    install(Koin) {
        slf4jLogger(level = Level.INFO)
        modules(koinModule)
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
}

private fun NettyApplicationEngine.Configuration.configureServer() {
    requestQueueLimit = 10
    runningLimit = 100
    shareWorkGroup = false
    responseWriteTimeoutSeconds = 2
    tcpKeepAlive = true

    callGroupSize = 10
    connectionGroupSize = 10
    workerGroupSize = 10
}