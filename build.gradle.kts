plugins {
  id("com.diffplug.spotless") version "7.0.4"
  id("net.ltgt.errorprone") version "5.1.0" apply false
}

repositories {
  mavenCentral()
}

spotless {
  flexmark {
    target("**/*.md")
    targetExclude("**/build/**")
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
    "testImplementation"(platform("org.junit:junit-bom:5.12.1"))
    "testImplementation"("org.junit.jupiter:junit-jupiter")
    "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    "implementation"("com.google.guava:guava:33.4.0-jre")
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
