import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("jvm") version "1.4.31"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    id("com.github.johnrengelman.shadow") version "6.1.0" apply false
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    repositories {
        mavenCentral()
    }

    configure<KtlintExtension> {
        version.set("0.40.0")
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "com.github.johnrengelman.shadow")

    project.version = "1.0.0"

    val shadowImplementation: Configuration by configurations.creating
    configurations["implementation"].extendsFrom(shadowImplementation)

    dependencies {
        shadowImplementation(kotlin("stdlib"))
    }

    tasks.withType<ShadowJar> {
        configurations = listOf(shadowImplementation)
        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveClassifier.set("")
    }
}
