rootProject.name = "commitlint-java"

pluginManagement {
  plugins {
    id("com.vanniktech.maven.publish") version "0.36.0"
  }
}

include("core")
include("cli")
include("maven-plugin")
include("gradle-plugin")
