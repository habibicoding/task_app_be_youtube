package com.example.task_app_be_youtube.service

import com.example.task_app_be_youtube.data.Task
import com.example.task_app_be_youtube.data.model.TaskCreateRequest
import com.example.task_app_be_youtube.data.model.TaskDto
import com.example.task_app_be_youtube.exception.TaskNotFoundException
import com.example.task_app_be_youtube.repository.TaskRepository
import org.springframework.stereotype.Service

@Service
class TaskService(private val repository: TaskRepository) {

    private fun mappingEntityToDto(task: Task) = TaskDto(
        task.id,
        task.description,
        task.isReminderSet,
        task.isTaskOpen,
        task.createdOn,
        task.priority
    )

    private fun mappingFromRequestToEntity(task: Task, request: TaskCreateRequest) {
        task.description = request.description
        task.isReminderSet = request.isReminderSet
        task.isTaskOpen = request.isTaskOpen
        task.priority = request.priority
    }

    private fun checkTaskForId(id: Long) {
        if (!repository.existsById(id)) {
            throw TaskNotFoundException("Task with the ID: $id does not exist!")
        }
    }


}