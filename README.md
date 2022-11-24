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

Tixte4J strives to provide a clean and full wrapping of the Tixte API for Java.

## Summary

1. [Introduction](#introduction)
2. [Download](#download)
3. [Documentation](#documentation)
4. [Support](#getting-help)
5. [Contribution](#contribution)
6. [Dependencies](#dependencies)
7. [Related projects](#tixte4j-related-projects)

## Introduction

Creating the `TixteClient` object is done via the `TixteClientBuilder` class. After setting the API-key, the session-token and other options via setters,
the `TixteClient` object is then created by calling the `build()` method.

**Example**:

```java
final TixteClient client = new TixteClientBuilder().create("API_KEY").setSessionToken("SESSION_TOKEN").build();
```

### Configuration

The `TixteClientBuilder` allows a set of configurations to improve the experience.

**Example**:

```java
public static void main(@NotNull String[] args) throws IOException
{
    // Creates a *new* TixteClientBuilder instance.
    final TixteClientBuilder builder = new TixteClientBuilder()
            // Sets the API-key, which is required for most of the methods.
            // This method also sets the cache policy. I really recommend to set this to ALL.
            // If this is equal to null or not set, this will be automatically set to NONE.
            // If you use this method like here, you don't have to set it via setCachePolicy(@Nullable CachePolicy).
            .create(getAPIKey(), CachePolicy.ALL)
            // Sets the session-token. (Optional but recommended)
            .setSessionToken(getSessionToken())
            // Sets a default domain. (Optional)
            .setDefaultDomain(getDefaultDomain());

    // Builds a *new* TixteClient instance and uses the provided API-key and session-token to start the login process.
    builder.build();
}
```

> See [TixteClientBuilder](https://blockydotjar.github.io/Tixte-Java-Library/tixte4j/dev/blocky/library/tixte/api/TixteClientBuilder.html) class

### More Examples

We provide a small set of examples in our [example directory](https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/src/test/).

## Experimental annotation

If a class or method is marked with the `Experimental` annotation, you should first check, if you have access to this experiment, because otherwise there could appear a `403 Forbidden` HTTP-code or there will be thrown a `Forbidden` exception.
<br>Even if you have access to the experiment, you should be careful, because there could appear some bugs/error/exceptions.

## Tixte experiments

If you have early access to a feature that I don't have, feel free to implement this into Tixte4J!

**Requirements:**
* Good Java knowledge
* At least one experimental feature
* Good knowledge about the internal code of Tixte4J

You also should read the [contribution guidelines](https://github.com/BlockyDotJar/Tixte-Java-Library/wiki/Contributing) first.

**Current Tixte experiments:**

```bf
FILE_SEARCH, SHARED_SUBDOMAINS, DOMAIN_FILE_TRANSFER, VIEW_FOLDERS, CREATE_FOLDERS, PER_DOMAIN_CONFIG, BILLING_PAYMENT_SOURCES
```

## Tixte turbo(-charged)

Some features are requiring a Tixte turbo(-charged) subscription, so be careful.
If you have access to turob(-charged) features that I don't have access to, feel free to implement this into Tixte4J!

**Requirements:**
* Good Java knowledge
* A Tixte turbo(-charged) subscription
* Good knowledge about the internal code of Tixte4J

If you own a Tixte turbo(-charged) subscription, thats cool!
<br>This is the case, because we also provide some examples for the 'Page Design' tab!

### Examples

**Name:** `MacOS-Big-Sur-Theme`
<br>**Author:** `Cole#8888`
<br>**Description:** `This theme re-creates the modern design of macOS big sur, right on your upload page! Pretty pog if I do say so myself.`

Click [here](https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/src/test/resources/MacOS-Big-Sur-Theme.css) to get the `CSS` file!

<p>
    <img src="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/assets/MacOS-Big-Sur-Theme-preview.png" width="500"/>
</p>

<hr>

**Name:** `Galactic-Waves`
<br>**Author:** `Swinsor#0001`
<br>**Description:** `Under the sea? Nahhh, ontop of the universe mate. How snazzy and trendy is this theme?`

Click [here](https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/src/test/resources/Galactic-Waves.css) to get the `CSS` file!

<p>
    <img src="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/assets/Galactic-Waves-Theme-preview.gif" width="500"/>
</p>

<hr>

**Name:** `Princess-Peach`
<br>**Author:** `Swinsor#0001`
<br>**Description:** `Who doesn't like a bit of pink in their lives? Well now you can have it on your files 😉`

Click [here](https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/src/test/resources/Princess-Peach.css) to get the `CSS` file!

<p>
    <img src="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/assets/Princess-Peach-Theme-preview.png" width="500"/>
</p>

<hr>

**Name:** `Galaxy-Mountain`
<br>**Author:** `Person0z#0812`
<br>**Description:** `Galaxy theme for those who love outer space 🚀🌌`

Click [here](https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/src/test/resources/Galaxy%20Mountain.css) to get the `CSS` file!

<p>
    <img src="https://github.com/BlockyDotJar/Tixte-Java-Library/blob/main/assets/Galaxy-Mountain-Theme-preview.jpg" width="500"/>
</p>

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

Be sure to replace the **`GITHUB_USERNAME`** key below with your GitHub username and **`GITHUB_TOKEN`** with a [GitHub token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)!

**Gradle Groovy:**

```gradle
repositories {
    maven {
        url = uri 'https://maven.pkg.github.com/BlockyDotJar/Tixte-Java-Library'
        credentials {
            username = project.findProperty('gpr.user') ?: 'GITHUB_USERNAME'
            password = project.findProperty('gpr.key') ?: 'GITHUB_TOKEN'
        }
    }
}
```

```gradle
dependencies {
    implementation 'dev.blocky.library:tixte4j:VERSION'
}
```

**Kotlin DSL:**

```gradle
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/BlockyDotJar/Tixte-Java-Library")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: "GITHUB_USERNAME"
            password = project.findProperty("gpr.key") as String? ?: "GITHUB_TOKEN"
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
SLF4J: No SLF4J providers were found.
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#noProviders for further details.
```
Or if you use an outdated version of it, something like this will appear in your console:

```php
SLF4J: No SLF4J providers were found.
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#noProviders for further details.
SLF4J: Class path contains SLF4J bindings targeting slf4j-api versions prior to 1.8.
SLF4J: Ignoring binding found at [jar:file:/C:/Users/UserName/.gradle/caches/modules-2/files-2.1/ch.qos.logback/logback-classic/1.2.11/4741689214e9d1e8408b206506cbe76d1c6a7d60/logback-classic-1.2.11.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#ignoredBindings for an explanation.
```

Tixte4J is currently providing a fallback logger in case that no SLF4J implementation is present.
We strongly recommend to use one though, as that can improve speed and allows you to customize the logger as well as logging to some files.

There is a guide for logback-classic available at the Tixte4J wiki: [Logging-Setup](https://github.com/BlockyDotJar/Tixte-Java-Library/wiki/Setup#logging).

## Documentation

JavaDocs are available in both jar format and web format.

The jar format is available on the [promoted downloads](https://github.com/BlockyDotJar/Tixte-Java-Library/packages/1520119) page or on any of the build pages of the [downloads](https://BlockyDotJar.github.io/Tixte-Java-Library).

## Getting help

For general troubleshooting you can visit our wiki [troubleshooting](https://github.com/BlockyDotJar/Tixte-Java-Library/wiki/Troubleshooting) and [FAQ](https://github.com/BlockyDotJar/Tixte-Java-Library/wiki/FAQ).
<br>If you need help, or just want to talk with the Tixte4J or other devs, you can join the [offical Tixte4J Discord server][discord-invite].

For guides and setup help you can also take a look at the wiki.
<br>Especially interesting are the [getting started](https://github.com/BlockyDotJar/Tixte-Java-Library/wiki/Getting-started) and [setup](https://github.com/BlockyDotJar/Tixte-Java-Library/wiki/Setup) pages.

## Contribution

If you want to contribute to Tixte4J, make sure to base your branch off of our **developer** branch
and create your PR into that **same** branch.
<br>**We will be rejecting any PRs, which are not based to the developer branch!**
<br>It is very possible that your change might already be in development, or you missed something.

More information can be found at the [contributing](https://github.com/BlockyDotJar/Tixte-Java-Library/wiki/Contributing) wiki page.

### Deprecation Policy

When a feature is introduced to replace or enhance existing functionality we might deprecate old functionality.

A deprecated method/class usually has a replacement mentioned in its documentation which should be switched to.
<br>Deprecated functionality might or might not exist in the next minor release. (Hint: The minor version is the `MM` of `XX.MM.RR(-TT.ZZ)` in our version format)

It is possible that some features are deprecated without replacement, in this case the functionality is no longer supported by either the Tixte4J structure
due to fundamental changes or due to Tixte-API changes that cause it to be removed.

We highly recommend discontinuing usage of deprecated functionality and update by going through each minor release instead of jumping.
<br>For instance, when updating from version `1.0.0-beta.1` to version `1.0.0-rc.2` you should do the following:

- Update to `1.0.0-beta.ZZ` and check for deprecation
- Update to `1.0.0-rc.2` and check for deprecation

The `RR` in version `1.0.RR` should be replaced by the latest version that was published for `1.0`, you can find out which the latest
version was by looking at the [release page](https://github.com/BlockyDotJar/Tixte-Java-Library/releases).


## Dependencies:

This project requires **Java 19+**
<br>All dependencies are managed automatically by Gradle.

* okhttp
    * Version: **v4.10.0**
    * [Github](https://github.com/square/okhttp)
* okio
    * Version: **v3.2.0**
    * [Github](https://github.com/square/okio)
* jackson
    * Version: **v2.14.1**
    * [Github](https://github.com/FasterXML/jackson)
* slf4j-api
    * Version: **v2.0.4**
    * [Github](https://github.com/qos-ch/slf4j)
* jetbrains-annotations
    * Version: **v23.0.0**
    * [Github](https://github.com/JetBrains/java-annotations)
* error-prone-annotations
    * Version: **v2.16**
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
