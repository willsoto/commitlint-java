plugins {
  application
}

dependencies {
  implementation(project(":core"))
}

application {
  mainClass.set("com.tiltedwindmills.commitlint.cli.Main")
}

tasks.named<JavaExec>("run") {
  workingDir = rootProject.projectDir
}

tasks.withType<JacocoCoverageVerification> {
  classDirectories.setFrom(
    classDirectories.files.map {
      fileTree(it) {
        exclude("**/Main.class")
      }
    },
  )
}

tasks.withType<JacocoReport> {
  classDirectories.setFrom(
    classDirectories.files.map {
      fileTree(it) {
        exclude("**/Main.class")
      }
    },
  )
}
