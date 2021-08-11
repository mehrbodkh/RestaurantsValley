package com.mehrbod.map_module.model

import android.content.Context

data class MapBoxOptions(
    val applicationContext: Context,
    val token: String,
    val styleUrl: String
) : MapOptions