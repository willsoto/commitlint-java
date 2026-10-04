dependencies {
  implementation(project(":core"))
  compileOnly("org.apache.maven:maven-plugin-api:3.10.0")
  compileOnly("org.apache.maven.plugin-tools:maven-plugin-annotations:3.16.0")
  testImplementation("org.apache.maven:maven-plugin-api:3.10.0")
}
