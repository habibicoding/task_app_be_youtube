package com.example.task_app_be_youtube.controller

import com.example.task_app_be_youtube.data.Priority
import com.example.task_app_be_youtube.data.model.TaskCreateRequest
import com.example.task_app_be_youtube.data.model.TaskDto
import com.example.task_app_be_youtube.data.model.TaskUpdateRequest
import com.example.task_app_be_youtube.exception.TaskNotFoundException
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

    @Test
    fun `given all opens tasks endpoint is called then check for number of tasks`() {
        // given
        val taskDto = TaskDto(
            22,
            "buy Hummus",
            isReminderSet = false,
            isTaskOpen = true,
            createdOn = LocalDateTime.now(),
            priority = Priority.MEDIUM
        )
        val tasks = listOf(dummyTaskDto, taskDto)

        // when
        `when`(mockService.getAllOpenTasks()).thenReturn(tasks)
        val resultActions: ResultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/open-tasks"))

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().`is`(200))
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        resultActions.andExpect(jsonPath("$.size()").value(tasks.size))
    }

    @Test
    fun `given all closed tasks endpoint is called then check for number of tasks`() {
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
        `when`(mockService.getAllClosedTasks()).thenReturn(tasks)
        val resultActions: ResultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/closed-tasks"))

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().`is`(200))
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        resultActions.andExpect(jsonPath("$.size()").value(tasks.size))
    }

    @Test
    fun `given one task when get task by id is called then check for correct description`() {
        `when`(mockService.getTaskById(dummyTaskDto.id)).thenReturn(dummyTaskDto)
        val resultActions: ResultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/task/${dummyTaskDto.id}"))

        resultActions.andExpect(MockMvcResultMatchers.status().`is`(200))
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        resultActions.andExpect(jsonPath("$.id").value(dummyTaskDto.id))
        resultActions.andExpect(jsonPath("$.description").value(dummyTaskDto.description))
        resultActions.andExpect(jsonPath("$.isReminderSet").value(dummyTaskDto.isReminderSet))
        resultActions.andExpect(jsonPath("$.isTaskOpen").value(dummyTaskDto.isTaskOpen))
    }

    @Test
    fun `when task id does not exist then except is not found response`() {
        `when`(mockService.getTaskById(taskId)).thenThrow(TaskNotFoundException("Task with id: $taskId does not exist!"))
        val resultActions: ResultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/task/$taskId"))

        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `when get task by id is called with an character in the url then expect a bad request message`() {
        val resultActions: ResultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/task/13L"))

        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `given update task when task is updated then check for correct properties`() {
        val request = TaskUpdateRequest(
            dummyTaskDto.description,
            dummyTaskDto.isReminderSet,
            dummyTaskDto.isTaskOpen,
            dummyTaskDto.priority
        )

        `when`(mockService.updateTask(dummyTaskDto.id, request)).thenReturn(dummyTaskDto)
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/update/${dummyTaskDto.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        resultActions.andExpect(jsonPath("$.description").value(dummyTaskDto.description))
        resultActions.andExpect(jsonPath("$.isReminderSet").value(dummyTaskDto.isReminderSet))
        resultActions.andExpect(jsonPath("$.isTaskOpen").value(dummyTaskDto.isTaskOpen))
    }

    @Test
    fun `given create task request when task is created then check for the properties`() {
        val request = TaskCreateRequest(
            dummyTaskDto.description,
            dummyTaskDto.isReminderSet,
            dummyTaskDto.isTaskOpen,
            LocalDateTime.now(),
            dummyTaskDto.priority
        )

        `when`(mockService.createTask(request)).thenReturn(dummyTaskDto)
        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        resultActions.andExpect(jsonPath("$.description").value(dummyTaskDto.description))
        resultActions.andExpect(jsonPath("$.isReminderSet").value(dummyTaskDto.isReminderSet))
        resultActions.andExpect(jsonPath("$.isTaskOpen").value(dummyTaskDto.isTaskOpen))
    }

    @Test
    fun `given id for delete request when task is deleted then check for the response message`() {
        val expectedMessage = "Task with the ID: $taskId has been deleted."

        `when`(mockService.deleteTask(taskId)).thenReturn(expectedMessage)
        val resultActions: ResultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/delete/$taskId"))

        resultActions.andExpect(MockMvcResultMatchers.status().`is`(200))
        resultActions.andExpect(content().string(expectedMessage))
    }
}