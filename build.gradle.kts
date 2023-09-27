import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
    application
}

group = "com.github.ferumbot"
version = "1.0.0"

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("com.github.ferumbot.MainKt")
}

dependencies {
    val ktorServerVersion = "2.3.4"
    val kafkaVersion = "3.4.0"
    val koinVersion = "3.5.0"

    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

    implementation("io.ktor:ktor-server-core-jvm:$ktorServerVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorServerVersion")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktorServerVersion")
    implementation("io.ktor:ktor-server-default-headers-jvm:$ktorServerVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorServerVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorServerVersion")

    implementation("org.apache.kafka:kafka-streams:$kafkaVersion")
    testImplementation("org.apache.kafka:kafka-streams-test-utils:$kafkaVersion")

    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")

    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
}