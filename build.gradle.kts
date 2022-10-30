plugins {
    `java-library`
    `maven-publish`
}

publishing {
    repositories {
        maven {
            name = "tixte4j"
            url = uri("https://maven.pkg.github.com/BlockyDotJar/Tixte-Java-Library")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
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

    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/BlockyDotJar/SLF4J-Fallback-Logger")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4.2")

    api("com.squareup.okhttp3:okhttp:4.10.0")
    api("com.squareup.okio:okio-jvm:3.2.0")
    api("org.slf4j:slf4j-api:2.0.3")
    api("org.apache.commons:commons-collections4:4.4")
    api("commons-io:commons-io:2.11.0")

    compileOnly("com.google.errorprone:error_prone_annotations:2.16")
    compileOnly("org.jetbrains:annotations:23.0.0")
}

group = "dev.blocky.library"
version = "1.1.3"
description = "A wrapper for the Tixte API in Java."

java {
    sourceCompatibility = JavaVersion.VERSION_19

    withSourcesJar()
    withJavadocJar()
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Automatic-Module-Name"] = "tixte4j"
    }
}
