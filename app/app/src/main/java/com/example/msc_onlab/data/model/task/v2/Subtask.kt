package com.example.msc_onlab.data.model.task.v2

import com.example.msc_onlab.data.model.task.v2.subtask.UpdateSubtaskData

data class Subtask(
    val done: Boolean,
    val id: Int,
    val title: String
)

fun Subtask.toUpdateData(): UpdateSubtaskData {
    return UpdateSubtaskData(
        done = this.done,
        id = this.id,
        title = this.title
    )
}
