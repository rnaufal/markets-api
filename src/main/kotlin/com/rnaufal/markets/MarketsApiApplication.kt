package com.rnaufal.markets

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
@EnableReactiveMongoRepositories
class MarketsApiApplication

fun main(args: Array<String>) {
    runApplication<MarketsApiApplication>(*args)
}
