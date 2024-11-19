package com.example.msc_onlab.data.model.household

import com.example.msc_onlab.data.model.task.Data
import com.example.msc_onlab.data.model.task.patch.Subtask
import com.example.msc_onlab.data.model.task.patch.TaskPatchData

data class TaskDetailed(
    val _id: IdXXX,
    val creation_date: String,
    val description: String,
    val done: Boolean,
    val due_date: String,
    val responsible: Responsible,
    val subtasks: List<SubtaskX>,
    val title: String
)

fun TaskDetailed.getPathData(): TaskPatchData {
    return TaskPatchData(
        creation_date = this.creation_date,
        description = this.description,
        done = this.done,
        due_date = this.due_date,
        responsible_id = this.responsible._id.`$oid`,
        title = this.title,
        subtasks = this.subtasks.map { subtask ->
            Subtask(
                _id = subtask._id.`$oid`,
                done = subtask.done,
                title = subtask.title,
                type = subtask.type
            )
        }
    )
}