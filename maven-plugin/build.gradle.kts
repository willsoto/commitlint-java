dependencies {
  implementation(project(":core"))
  compileOnly("org.apache.maven:maven-plugin-api:3.9.16")
  compileOnly("org.apache.maven.plugin-tools:maven-plugin-annotations:3.15.2")
  testImplementation("org.apache.maven:maven-plugin-api:3.9.16")
}
