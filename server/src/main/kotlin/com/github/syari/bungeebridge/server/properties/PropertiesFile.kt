package com.github.syari.bungeebridge.server.properties

import java.io.File
import java.util.Properties

open class PropertiesFile(fileName: String) {
    private val file = File(fileName)
    private var properties: Properties
    open val comments = ""

    init {
        if (file.exists().not()) {
            file.createNewFile()
        }
        properties = Properties().apply { load(file.inputStream()) }
    }

    fun keyValue(key: String) = PropertiesValue(properties, key)

    fun save() {
        if (file.exists().not()) {
            file.createNewFile()
        }
        properties.store(file.outputStream(), comments)
    }
}
