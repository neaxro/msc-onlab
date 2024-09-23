package com.example.msc_onlab.helpers

import com.auth0.android.jwt.JWT

object LoggedPersonData {
    private lateinit var jwt: JWT

    var ID: String? = null
    var USERNAME: String? = null
    var TOKEN: String = ""
        set(value) {
            jwt = JWT(value)
            this.ID = jwt.getClaim("id").asString()
            this.USERNAME = jwt.getClaim("username").asString()

            field = value
        }
}