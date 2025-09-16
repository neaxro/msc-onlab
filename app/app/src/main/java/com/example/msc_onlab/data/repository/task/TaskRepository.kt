package com.example.msc_onlab.data.repository.task

import com.example.msc_onlab.data.model.task.v2.GetTasksResponseItem
import com.example.msc_onlab.data.model.task.v2.TasksResponse
import com.example.msc_onlab.data.model.task.v2.create.CreateTaskData
import com.example.msc_onlab.data.model.task.v2.create.CreateTaskResponse
import com.example.msc_onlab.data.model.task.v2.subtask.CreateSubtaskData
import com.example.msc_onlab.data.model.task.v2.subtask.CreateSubtaskResponse
import com.example.msc_onlab.data.model.task.v2.subtask.UpdateSubtaskData
import com.example.msc_onlab.data.model.task.v2.update.UpdateTaskData
import com.example.msc_onlab.domain.wrappers.Resource
import retrofit2.Response

interface TaskRepository {
    suspend fun getTasks(teamId: Int): Resource<TasksResponse>

    suspend fun getTask(taskId: Int): Resource<GetTasksResponseItem>

    suspend fun createTask(taskData: CreateTaskData): Resource<CreateTaskResponse>

    suspend fun updateTask(updateData: UpdateTaskData): Resource<Unit>

    suspend fun deleteTask(taskId: Int): Resource<Unit>

    suspend fun createSubtask(subtask: CreateSubtaskData): Resource<CreateSubtaskResponse>

    suspend fun updateSubtask(updateSubtaskData: UpdateSubtaskData): Resource<Unit>

    suspend fun deleteSubtask(subtaskId: Int): Resource<Unit>
}