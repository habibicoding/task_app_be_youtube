package com.example.task_app_be_youtube.service

import com.example.task_app_be_youtube.data.Priority
import com.example.task_app_be_youtube.data.Task
import com.example.task_app_be_youtube.data.model.TaskCreateRequest
import com.example.task_app_be_youtube.data.model.TaskDto
import com.example.task_app_be_youtube.repository.TaskRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
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

}