plugins {
  `java-library`
  `maven-publish`
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
    }
  }
}
