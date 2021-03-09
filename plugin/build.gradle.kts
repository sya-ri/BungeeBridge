import net.minecrell.pluginyml.bungee.BungeePluginDescription

plugins {
    id("net.minecrell.plugin-yml.bungee") version "0.3.0"
}

repositories {
    maven(url = "https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    shadowImplementation(project(":shared"))
    shadowImplementation("io.ktor:ktor-client-cio:1.5.2")
    shadowImplementation("io.ktor:ktor-client-jackson:1.5.2")
    implementation("net.md-5:bungeecord-api:1.16-R0.4")
}

configure<BungeePluginDescription> {
    name = rootProject.name
    main = "com.github.syari.bungeebridge.plugin.BungeeBridge"
    description = "Share Player Information With Other Bungeecord Servers"
    author = "sya_ri"
    softDepends = setOf("cmd_list")
}
