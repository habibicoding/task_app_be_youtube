package com.example.task_app_be_youtube

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaskAppBeYoutubeApplication

fun main(args: Array<String>) {
    runApplication<TaskAppBeYoutubeApplication>(*args)
    println("Marhaba ya shabaab /مرحبا يا شباب")
}
