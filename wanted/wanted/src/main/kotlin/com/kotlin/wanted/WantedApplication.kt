package com.kotlin.wanted

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
class WantedApplication

fun main(args: Array<String>) {
    runApplication<WantedApplication>(*args)
}
