package com.example.msc_onlab.data.model.task

import com.example.msc_onlab.data.model.task.patch.TaskPatchData
import com.example.msc_onlab.data.model.task.patch.Subtask as PatchSubtaskData

data class Data(
    val _id: Id,
    val creation_date: String,
    val description: String,
    val done: Boolean,
    val due_date: String,
    val responsible_id: ResponsibleId,
    val subtasks: List<Subtask>,
    val title: String
)

fun Data.getPathData(): TaskPatchData {
    return TaskPatchData(
        creation_date = this.creation_date,
        description = this.description,
        done = this.done,
        due_date = this.due_date,
        responsible_id = this.responsible_id.`$oid`,
        title = this.title,
        subtasks = this.subtasks.map { subtask ->
            PatchSubtaskData(
                _id = subtask._id.`$oid`,
                done = subtask.done,
                title = subtask.title,
                type = subtask.type
            )
        }
    )
}
