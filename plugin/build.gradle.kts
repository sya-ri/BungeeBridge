import net.minecrell.pluginyml.bungee.BungeePluginDescription

plugins {
    id("net.minecrell.plugin-yml.bungee") version "0.3.0"
}

repositories {
    maven(url = "https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    implementation("net.md-5:bungeecord-api:1.16-R0.4")
}

configure<BungeePluginDescription> {
    name = rootProject.name
    main = "com.github.syari.bungeebridge.plugin.BungeeBridge"
    description = "Share Player Information With Other Bungeecord Servers"
    author = "sya_ri"
}
