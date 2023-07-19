package com.speechify.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestApplication

fun main(args: Array<String>) {
	runApplication<TestApplication>(*args)
	val projectPath = System.getProperty("user.dir")
	println("prueba"+projectPath)
}
