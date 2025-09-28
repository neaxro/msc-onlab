package com.example.msc_onlab.data.model.invitation

import com.example.msc_onlab.data.model.profile.Attributes
import com.example.msc_onlab.data.model.task.v2.Responsible
import kotlin.Any
import kotlin.collections.List

data class FindUserResponse(
    val attributes: Attributes,
    val disableableCredentialTypes: List<Any>,
    val email: String,
    val emailVerified: Boolean,
    val enabled: Boolean,
    val firstName: String,
    val id: String,
    val lastName: String,
    val notBefore: Int,
    val requiredActions: List<Any>,
    val totp: Boolean,
    val username: String
)

fun FindUserResponse.toResponsible(): Responsible {
    return Responsible(
        disableableCredentialTypes = this.disableableCredentialTypes,
        email = this.email,
        emailVerified = this.emailVerified,
        enabled = this.enabled,
        firstName = this.firstName,
        id = this.id,
        lastName = this.lastName,
        notBefore = this.notBefore,
        requiredActions = this.requiredActions,
        totp = this.totp,
        username = this.username,
        attributes = this.attributes
    )
}