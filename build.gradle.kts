import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

publishing {
    repositories {
        maven {
            name = "tixte4j"
            url = uri("https://maven.pkg.github.com/BlockyDotJar/Tixte-Java-Library")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_PASSWORD")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api("com.squareup.okhttp3:okhttp:4.10.0")
    api("com.squareup.okio:okio-jvm:3.2.0")
    api("org.slf4j:slf4j-api:2.0.0")
    api("org.jetbrains:annotations:23.0.0")
    api("com.google.errorprone:error_prone_annotations:2.15.0")
    api("org.apache.commons:commons-collections4:4.4")
    api("commons-io:commons-io:2.11.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
}

group = "dev.blocky.library"
version = "1.0.1"
description = "A wrapper for the Tixte API in Java."

java {
    sourceCompatibility = JavaVersion.VERSION_1_8

    withSourcesJar()
    withJavadocJar()
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Automatic-Module-Name"] = "dev.blocky.library.tixte"
    }
}

val shadowJar by tasks.getting(ShadowJar::class) {
    minimize()
}
