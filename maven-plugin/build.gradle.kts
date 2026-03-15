dependencies {
  implementation(project(":core"))
  compileOnly("org.apache.maven:maven-plugin-api:3.9.9")
  compileOnly("org.apache.maven.plugin-tools:maven-plugin-annotations:3.15.1")
  testImplementation("org.apache.maven:maven-plugin-api:3.9.9")
}
