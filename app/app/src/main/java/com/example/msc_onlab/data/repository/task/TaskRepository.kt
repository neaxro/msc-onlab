package com.example.msc_onlab.data.repository.task

import com.example.msc_onlab.data.model.task.v2.TasksResponse
import com.example.msc_onlab.domain.wrappers.Resource

interface TaskRepository {
    suspend fun getTasks(teamId: Int): Resource<TasksResponse>
}