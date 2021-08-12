package com.mehrbod.restaurantsvalley.data.model

import com.google.gson.annotations.SerializedName

data class Stats(
    val tipCount: Int,
    val usersCount: Int,
    val checkinsCount: Int
)