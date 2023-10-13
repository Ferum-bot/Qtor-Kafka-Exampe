package com.github.ferumbot

import com.github.ferumbot.api.withKitchenApi
import com.github.ferumbot.services.CookExecutorService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.ServerReady
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.Slf4JLoggerFactory
import kotlinx.serialization.json.Json
import org.koin.core.logger.Level
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

private val logger = KotlinLogging.logger {  }

fun main(args: Array<String>) {

    InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.INSTANCE)

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
        val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
        registry = prometheusRegistry
        meterBinders = listOf(
            JvmThreadMetrics(),
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            ProcessorMetrics()
        )
        this@kitchenModule.routing {
            get("/metrics"){
                call.respond(prometheusRegistry.scrape())
            }
        }
    }
    install(Koin) {
        slf4jLogger(level = Level.INFO)
        modules(kitchenModule, kafkaModule)
        createEagerInstances()
    }
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            isLenient = true
        })
    }

    withKitchenApi()

    addConsumerExecutorServiceStarting()
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

private fun Application.addConsumerExecutorServiceStarting() {
    val executor by inject<CookExecutorService>()
    environment.monitor.subscribe(ServerReady) {
        executor.startExecution()
        logger.info { "Cock executor started" }
    }
    environment.monitor.unsubscribe(ApplicationStopped) {
        executor.terminateExecution()
        logger.info { "Cook executor stopped" }
    }
}