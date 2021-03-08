package com.github.syari.bungeebridge.shared

data class UpdateRequest(
    val name: String,
    val join: Set<String> = emptySet(),
    val quit: Set<String> = emptySet()
)
