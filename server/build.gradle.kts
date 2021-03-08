dependencies {
    shadowImplementation(project(":shared"))
    shadowImplementation("io.ktor:ktor-server-netty:1.5.2")
    shadowImplementation("io.ktor:ktor-jackson:1.5.2")
    shadowImplementation("ch.qos.logback:logback-classic:1.2.3")
}

tasks.getByName<Jar>("jar") {
    manifest {
        attributes(
            "Main-Class" to "com.github.syari.bungeebridge.server.BungeeBridge"
        )
    }
}
