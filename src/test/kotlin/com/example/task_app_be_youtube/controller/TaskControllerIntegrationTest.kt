package com.example.task_app_be_youtube.controller

import com.example.task_app_be_youtube.data.Priority
import com.example.task_app_be_youtube.data.model.TaskDto
import com.example.task_app_be_youtube.service.TaskService
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.time.LocalDateTime

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [TaskController::class])
class TaskControllerIntegrationTest(@Autowired private val mockMvc: MockMvc) {

    @MockBean
    private lateinit var mockService: TaskService

    private val taskId: Long = 77
    private val dummyTaskDto = TaskDto(
        77,
        "finish video series",
        isReminderSet = true,
        isTaskOpen = true,
        createdOn = LocalDateTime.now(),
        priority = Priority.LOW
    )
    private val mapper = jacksonObjectMapper()

    @BeforeEach
    fun setUp() {
        mapper.registerModule(JavaTimeModule())
    }

    @Test
    fun `given all tasks endpoint is called then check for number of tasks`() {
        // given
        val taskDto = TaskDto(
            22,
            "buy Hummus",
            isReminderSet = false,
            isTaskOpen = false,
            createdOn = LocalDateTime.now(),
            priority = Priority.MEDIUM
        )
        val tasks = listOf(dummyTaskDto, taskDto)

        // when
        `when`(mockService.getAllTasks()).thenReturn(tasks)
        val resultActions: ResultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/all-tasks"))

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().`is`(200))
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        resultActions.andExpect(jsonPath("$.size()").value(tasks.size))

    }
}