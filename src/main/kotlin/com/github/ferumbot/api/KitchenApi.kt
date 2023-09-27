package com.github.ferumbot.api

import io.ktor.server.application.Application
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.routing

fun Application.withKitchenApi() {
    routing {

        get("/kitchen/:dishId/status") {

        }

        get("/kitchen/dishes") {

        }

        put("/kitchen/cooking/start") {

        }
    }
}