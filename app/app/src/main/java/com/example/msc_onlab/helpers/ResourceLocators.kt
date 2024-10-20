package com.example.msc_onlab.helpers

import com.example.msc_onlab.R

object ResourceLocator{
    //val availableProfilePictures = listOf<String>("man_1", "man_2", "man_3", "woman_1", "woman_2", "woman_3")
    val availableProfilePictures = listOf<String>(
        "man_1", "man_2", "man_3", "woman_1", "woman_2", "woman_3",
        "man_1", "man_2", "man_3", "woman_1", "woman_2", "woman_3",
        "man_1", "man_2", "man_3", "woman_1", "woman_2", "woman_3",
        "man_1", "man_2", "man_3", "woman_1", "woman_2", "woman_3",
        "man_1", "man_2", "man_3", "woman_1", "woman_2", "woman_3",
    )

    fun getProfilePicture(profilePictureName: String): Int {
        return when(profilePictureName){
            "man_1" -> R.drawable.man_1
            "man_2" -> R.drawable.man_2
            "man_3" -> R.drawable.man_3
            "woman_1" -> R.drawable.woman_1
            "woman_2" -> R.drawable.woman_2
            "woman_3" -> R.drawable.woman_3

            else -> R.drawable.man_1
        }
    }
}
