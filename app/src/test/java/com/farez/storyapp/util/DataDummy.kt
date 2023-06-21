package com.farez.storyapp.util

import com.farez.storyapp.data.remote.response.Story

object DataDummy {
    fun generateDummy() : List<Story> {
        val stories : MutableList<Story> = arrayListOf()
        for (i in 0..149) {
            stories.add(
                Story(
                    "photoUrl $i", "createdAt + $i", "name $i",
                    "description $i", 0.0, "$i",
                    0.0,
                ))
        }
        return stories
    }
}