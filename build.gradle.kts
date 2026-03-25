plugins {
  id("com.diffplug.spotless") version "8.4.0"
  id("net.ltgt.errorprone") version "5.1.0" apply false
  id("com.vanniktech.maven.publish") apply false
}

repositories {
  mavenCentral()
}

spotless {
  flexmark {
    target("**/*.md")
    targetExclude("**/build/**", "CHANGELOG.md")
    flexmark()
  }
  kotlinGradle {
    target("**/*.gradle.kts")
    targetExclude("**/build/**")
    ktlint()
      .editorConfigOverride(mapOf("indent_size" to "2"))
  }
}

subprojects {
  apply(plugin = "java-library")
  apply(plugin = "com.diffplug.spotless")
  apply(plugin = "net.ltgt.errorprone")
  apply(plugin = "jacoco")

  group = property("group") as String
  version = property("version") as String

  configure<JavaPluginExtension> {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of(25))
    }
  }

  repositories {
    mavenCentral()
  }

  dependencies {
    "testImplementation"(platform("org.junit:junit-bom:6.0.3"))
    "testImplementation"("org.junit.jupiter:junit-jupiter")
    "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    "implementation"("com.google.guava:guava:33.5.0-jre")
    "implementation"("org.jspecify:jspecify:1.0.0")
    "errorprone"("com.google.errorprone:error_prone_core:2.48.0")
  }

  tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
      events("passed", "skipped", "failed")
      showStandardStreams = true
    }
    outputs.upToDateWhen { false }
  }

  spotless {
    java {
      googleJavaFormat("1.35.0")
    }
  }

  tasks.withType<JacocoReport> {
    dependsOn(tasks.withType<Test>())
  }

  tasks.withType<JacocoCoverageVerification> {
    dependsOn(tasks.withType<JacocoReport>())
    violationRules {
      rule {
        limit {
          counter = "LINE"
          minimum = "0.95".toBigDecimal()
        }
        limit {
          counter = "BRANCH"
          minimum = "0.95".toBigDecimal()
        }
      }
    }
  }

  tasks.named("check") {
    dependsOn(tasks.withType<JacocoCoverageVerification>())
  }
}

configure(subprojects.filter { it.name in listOf("core", "maven-plugin") }) {
  apply(plugin = "com.vanniktech.maven.publish")

  extensions.configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    coordinates(
      groupId = project.group.toString(),
      artifactId = when (project.name) {
        "core" -> "commitlint-core"
        else -> "commitlint-${project.name}"
      },
      version = project.version.toString(),
    )

    pom {
      name.set(
        when (project.name) {
          "core" -> "commitlint-core"
          else -> "commitlint-${project.name}"
        },
      )
      description.set("A Java port of commitlint — lint commit messages against the Conventional Commits format")
      inceptionYear.set("2026")
      url.set("https://github.com/willsoto/commitlint-java/")

      licenses {
        license {
          name.set("The Apache License, Version 2.0")
          url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
          distribution.set("repo")
        }
      }

      developers {
        developer {
          id.set("willsoto")
          name.set("Will Soto")
          url.set("https://github.com/willsoto/")
        }
      }

      scm {
        url.set("https://github.com/willsoto/commitlint-java/")
        connection.set("scm:git:git://github.com/willsoto/commitlint-java.git")
        developerConnection.set("scm:git:ssh://git@github.com/willsoto/commitlint-java.git")
      }
    }
  }
}
