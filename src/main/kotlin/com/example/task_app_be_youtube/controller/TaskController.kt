package com.example.task_app_be_youtube.controller

import com.example.task_app_be_youtube.data.model.TaskCreateRequest
import com.example.task_app_be_youtube.data.model.TaskDto
import com.example.task_app_be_youtube.data.model.TaskUpdateRequest
import com.example.task_app_be_youtube.service.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class TaskController(private val service: TaskService) {

    @GetMapping("all-tasks")
    fun getAllTasks(): ResponseEntity<List<TaskDto>> = ResponseEntity(service.getAllTasks(), HttpStatus.OK)

    @GetMapping("open-tasks")
    fun getAllOpenTasks(): ResponseEntity<List<TaskDto>> = ResponseEntity(service.getAllOpenTasks(), HttpStatus.OK)

    @GetMapping("closed-tasks")
    fun getAllClosedTasks(): ResponseEntity<List<TaskDto>> = ResponseEntity(service.getAllClosedTasks(), HttpStatus.OK)

    @GetMapping("task/{id}")
    fun getTaskById(@PathVariable id: Long): ResponseEntity<TaskDto> =
        ResponseEntity(service.getTaskById(id), HttpStatus.OK)

    @PostMapping("create")
    fun createTask(
        @Valid @RequestBody request: TaskCreateRequest
    ): ResponseEntity<TaskDto> = ResponseEntity(service.createTask(request), HttpStatus.OK)

    @PatchMapping("updaet/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @Valid @RequestBody request: TaskUpdateRequest
    ): ResponseEntity<TaskDto> = ResponseEntity(service.updateTask(id, request), HttpStatus.OK)

    @DeleteMapping("delete/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<String> =
        ResponseEntity(service.deleteTask(id), HttpStatus.OK)
}