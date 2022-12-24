package com.example.task_app_be_youtube.data.model

import com.example.task_app_be_youtube.data.Priority
import java.time.LocalDateTime

data class TaskDto(
    val id: Long,
    val description: String,
    val isReminderSet: Boolean,
    val isTaskOpen: Boolean,
    val createdOn: LocalDateTime,
    val priority: Priority
)
