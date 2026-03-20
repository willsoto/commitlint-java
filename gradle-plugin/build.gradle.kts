plugins {
  `java-gradle-plugin`
  id("com.gradle.plugin-publish") version "2.1.0"
}

dependencies {
  implementation(project(":core"))
  testImplementation(gradleTestKit())
}

gradlePlugin {
  website.set("https://github.com/willsoto/commitlint-java")
  vcsUrl.set("https://github.com/willsoto/commitlint-java.git")
  plugins {
    create("commitlint") {
      id = "com.tilted-windmills.commitlint"
      implementationClass = "com.tiltedwindmills.commitlint.gradle.CommitlintPlugin"
      displayName = "Commitlint Plugin"
      description = "Lint commit messages against the Conventional Commits format"
      tags.set(listOf("commitlint", "conventional-commits", "linting"))
    }
  }
}
