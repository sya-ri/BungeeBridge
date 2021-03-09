package com.github.syari.bungeebridge.shared

data class UpdateRequest(
    val name: String,
    val players: Map<String, String>
)
