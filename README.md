[github-packages-shield]: https://img.shields.io/github/v/release/BlockyDotJar/Tixte-Java-Library
[github-packages]: https://github.com/BlockyDotJar/Tixte-Java-Library/packages/1520119

[license-shield]: https://img.shields.io/badge/License-Apache%202.0-white.svg
[license]: https://github.com/BlockyDotJar/Tixte-Java-Library/tree/main/LICENSE

[discord-invite-shield]: https://discord.com/api/guilds/876766868864647188/widget.png
[discord-invite]: https://discord.gg/mYKK4BwGxe

[download]: #download

<img align="right" src="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/assets/Tixte-Logo.png?raw=true" height="200" width="200">

# Tixte4J (Tixte for Java)

[ ![github-packages-shield][] ][download] [ ![license-shield][] ][license] [ ![discord-invite-shield][] ][discord-invite]

**What is Tixte4J (Tixte for Java)?**

Tixte4J strives to provide a clean and full wrapping of the Tixte API for Java.

## Summary

1. [Introduction](#introduction)
2. [Download](#download)
3. [Documentation](#documentation)
4. [Support](#getting-help)
5. [Dependencies](#dependencies)
6. [Other libraries](#tixte4j-related-projects)

## Introduction

Creating the `TixteClient` object is done via the `TixteClientBuilder` class. After setting the API-key, the session-token and other options via setters,
the `TixteClient` object is then created by calling the `build()` method.

**Example**:

```java
TixteClient client = new TixteClientBuilder().create("API_KEY").setSessionToken("SESSION_TOKEN").build();
```

### Configuration

The `TixteClientBuilder` allows a set of configurations to improve the experience.

**Example**:

```java
    public static void main(@NotNull String[] args) throws IOException
    {
        // Creates a new TixteClient.
        TixteClientBuilder builder = new TixteClientBuilder()
        // Sets the API-key, which is required for most of the methods.
        // This method also sets the cache policy. I really recommend to set this to ALL.
        // If this is equal to null or not set, this will be automatically set to NONE.
        // If you use this method like here, you don't have to set it via setCachePolicy(@Nullable CachePolicy).
        .create(getAPIKey(), CachePolicy.ALL)
        // Sets the session-token. (Optional but recommended)
        .setSessionToken(getSessionToken())
        // Sets a default domain. (Optional)
        .setDefaultDomain(getDefaultDomain());

        // Builds a new TixteClient instance and uses the provided API-key and session-token to start the login process.
        client = builder.build();
    }
```

> See [TixteClientBuilder](https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/src/main/java/dev/blocky/library/tixte/api/TixteClientBuilder.java)

## Download

[ ![github-packages-shield][] ][github-packages]

Find the latest GitHub release [here](https://github.com/BlockyDotJar/Tixte-Java-Library/releases/latest).

Be sure to replace the **`VERSION`** key below with the one of the versions shown above!

### Maven

First you should create a `settings.xml` and add this to it:
<br> Be sure to replace the **`GITHUB_USERNAME`** key below with your GitHub username and **`GITHUB_TOKEN`** with a [GitHub token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)!

```xml
<servers>
  <server>
    <id>github</id>
    <username>GITHUB_USERNAME</username>
    <password>GITHUB_TOKEN</password>
  </server>
</servers>
```

After that you add this repository and dependency to your `pom.xml`:

```xml
<repositories>
  <repository>
    <id>github</id>
    <name>GitHub BlockyDotJar Apache Maven Packages</name>
    <url>https://maven.pkg.github.com/BlockyDotJar/Tixte-Java-Library</url>
  </repository>
</repositories>
```

```xml
<dependencies>
  <dependency>
    <groupId>dev.blocky.library</groupId>
    <artifactId>tixte4j</artifactId>
    <version>VERSION</version>
  </dependency>
</dependencies>
```

### Gradle

Be sure to replace the **`GITHUB_USERNAME`** key below with your GitHub username and **`GITHUB_TOKEN`** with a [GitHub token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-tokenn)!

```gradle
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/BlockyDotJar/Tixte-Java-Library")
        credentials {
            username = project.findProperty("gpr.user") ?: "GITHUB_USERNAME"
            password = project.findProperty("gpr.key") ?: "GITHUB_TOKEN"
        }
    }
}
```

```gradle
dependencies {
    implementation("dev.blocky.library:tixte4j:VERSION")
}
```

### Logging framework - SLF4J

Tixte4J is using [SLF4J](https://www.slf4j.org/) to log its messages.

That means you should add some SLF4J implementation to your build path in addition to Tixte4J.
If no implementation is found, following message will be printed to the console on startup:

```php
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
```

Tixte4J is currently providing a fallback logger in case that no SLF4J implementation is present.
We strongly recommend to use one though, as that can improve speed and allows you to customize the logger as well as logging to some files.

There is a guide for logback-classic available at the JDA wiki: [Logging Setup](https://github.com/BlockyDotJar/Tixte-Java-Library/wiki/Logging).

## Documentation

***Docs are currently not available***
<br>See JavaDocs.

## Getting help

Troubleshooting or FAQ pages will be available soon!
<br>If you need help, or just want to talk with the Tixte4J or other devs, you can join the [support server][discord-invite].
## Dependencies:

This project requires **Java 8+**

* okhttp
    * Version: **v4.10.0**
    * [Github](https://github.com/square/okhttp)
* okio
    * Version: **3.2.0**
    * [Github](https://github.com/square/okio/)
* jackson
    * Version: **v2.13.3**
    * [Github](https://github.com/stleary/JSON-java)
* slf4j-api
    * Version: **v1.7.36**
    * [Github](https://github.com/qos-ch/slf4j)
* jetbrains-annotations
    * Version: **v23.0.0**
    * [Github](https://github.com/JetBrains/java-annotations)
* error-prone-annotations
    * Version: **v2.15.0**
    * [Github](https://github.com/google/error-prone)
* commons-collections4
    * Version: **v4.4**
    * [Github](https://github.com/apache/commons-collections)
* commons-io
    * Version: **v2.11.0** 
    * [Github](https://github.com/apache/commons-io)

## Tixte4J related projects

- [tixte-node](https://github.com/UltiRequiem/tixte-node)
- [tixte.py](https://github.com/NextChai/tixte.py)
- [tixte.js](https://github.com/macedonga/tixte.js)