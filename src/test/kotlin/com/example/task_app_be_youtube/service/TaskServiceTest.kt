package com.example.task_app_be_youtube.service

import com.example.task_app_be_youtube.data.Priority
import com.example.task_app_be_youtube.data.Task
import com.example.task_app_be_youtube.data.model.TaskCreateRequest
import com.example.task_app_be_youtube.data.model.TaskDto
import com.example.task_app_be_youtube.exception.BadRequestException
import com.example.task_app_be_youtube.repository.TaskRepository
import io.mockk.MockKAnnotations
import io.mockk.called
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class TaskServiceTest {

    @RelaxedMockK
    private lateinit var mockRepository: TaskRepository

    @InjectMockKs
    private lateinit var objectUnderTest: TaskService

    private val task = Task()
    private lateinit var createRequest: TaskCreateRequest

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        createRequest = TaskCreateRequest(
            "test",
            isReminderSet = false,
            isTaskOpen = false,
            createdOn = LocalDateTime.now(),
            priority = Priority.LOW
        )
    }

    @Test
    fun `when all tasks get fetched then check if then given size is correct`() {
         // given
        val expectedTasks: List<Task> = listOf(Task(), Task())

        // when
        every { mockRepository.findAll() } returns expectedTasks.toMutableList()
        val actualTasks: List<TaskDto> = objectUnderTest.getAllTasks()

        // then
        assertThat(actualTasks.size).isEqualTo(expectedTasks.size)
    }

    @Test
    fun `when task is created then check for the task properties`() {
        // given
        task.description = createRequest.description
        task.isTaskOpen = createRequest.isTaskOpen
        task.priority = createRequest.priority

        // when
        every { mockRepository.save(any()) } returns task
        val actualTaskDto: TaskDto = objectUnderTest.createTask(createRequest)

        // then
        assertThat(actualTaskDto.description).isEqualTo(task.description)
        assertThat(actualTaskDto.isTaskOpen).isEqualTo(task.isTaskOpen)
        assertThat(actualTaskDto.priority).isEqualTo(task.priority)
    }

    @Test
    fun `when task description already exists then check for bad request exception`() {
        // given
        // when
        every { mockRepository.doesDescriptionExist(any()) } returns true
        val exception  = assertThrows<BadRequestException> {objectUnderTest.createTask(createRequest)}

        // then
        assertThat(exception.message).isEqualTo("There is already a task with the description: ${createRequest.description}")
        verify { mockRepository.save(any()) wasNot called }
    }

}