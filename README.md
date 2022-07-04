[github-packages-shield]: https://img.shields.io/github/v/release/BlockyDotJar/Tixte-Java-Library
[github-packages]: https://github.com/BlockyDotJar/Tixte-Java-Library/packages/1365003

## Tixte Java Library

Note that this library is in beta at the time and that there are not much documentations about it.

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
