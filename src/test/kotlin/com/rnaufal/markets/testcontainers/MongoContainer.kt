package com.rnaufal.markets.testcontainers

import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.MongoDBContainer

object MongoContainer : MongoDBContainer("mongo:latest") {

    fun configure(registry: DynamicPropertyRegistry) {
        registry.add("spring.data.mongodb.uri") { getReplicaSetUrl("markets-api") }
    }

    override fun stop() {
        println("Stopping MongoDB...")
    }
}
