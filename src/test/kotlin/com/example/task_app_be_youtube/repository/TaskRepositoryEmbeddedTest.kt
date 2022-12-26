package com.example.task_app_be_youtube.repository

import com.example.task_app_be_youtube.data.Task
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.jdbc.Sql

@DataJpaTest(properties = ["spring.jpa.properties.javax.persistence.validation.mode=none"])
class TaskRepositoryEmbeddedTest {

    @Autowired
    private lateinit var objectUnderTest: TaskRepository

    private val numberOfRecordsInTestDataSql = 3
    private val numberOfOpenRecordsInTestDataSql = 1
    private val numberOfClosedRecordsInTestDataSql = 2

    @Test
    @Sql("classpath:test-data.sql")
    fun `when task is saved then check for not null`() {
        // Given
        // Then
        val task: Task = objectUnderTest.findTaskById(111)

        // Then
        assertThat(task).isNotNull
    }
}