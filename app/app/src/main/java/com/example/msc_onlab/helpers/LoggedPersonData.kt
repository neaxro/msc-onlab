package com.example.msc_onlab.helpers

import com.auth0.android.jwt.JWT
import com.example.msc_onlab.R

object LoggedPersonData {
    private lateinit var jwt: JWT

    var ID: String? = null
    var USERNAME: String? = null
    var TOKEN: String = ""
        set(value) {
            jwt = JWT(value)
            this.ID = jwt.getClaim("id").asString()
            this.USERNAME = jwt.getClaim("username").asString()
            this.PROFILE_PICTURE = jwt.getClaim("profile_picture").asString()

            field = value
        }

    var SELECTED_HOUSEHOLD_ID: String? = null
    var PROFILE_PICTURE: String? = null
}

fun LoggedPersonData.clear(){
    this.ID = null
    this.USERNAME = null
    this.TOKEN = ""
    this.SELECTED_HOUSEHOLD_ID = null
    this.PROFILE_PICTURE = null
}
