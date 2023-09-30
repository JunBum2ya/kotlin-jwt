package com.kotlin.wanted

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WantedApplication

fun main(args: Array<String>) {
    runApplication<WantedApplication>(*args)
}
