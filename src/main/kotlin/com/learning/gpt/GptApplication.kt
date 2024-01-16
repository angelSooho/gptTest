package com.learning.gpt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GptApplication

fun main(args: Array<String>) {
	runApplication<GptApplication>(*args)
}
