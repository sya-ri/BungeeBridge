dependencies {
    shadowImplementation("ch.qos.logback:logback-classic:1.2.3")
}

tasks.getByName<Jar>("jar") {
    manifest {
        attributes(
            "Main-Class" to "com.github.syari.bungeebridge.server.BungeeBridge"
        )
    }
}
