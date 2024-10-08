package com.example.msc_onlab.data.model.members

data class MembersResponse(
    val `data`: List<MemberData>,
    val no_data: Int,
    val status: String,
    val time: String
)