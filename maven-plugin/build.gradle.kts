dependencies {
  implementation(project(":core"))
  compileOnly("org.apache.maven:maven-plugin-api:3.9.14")
  compileOnly("org.apache.maven.plugin-tools:maven-plugin-annotations:3.15.2")
  testImplementation("org.apache.maven:maven-plugin-api:3.9.14")
}
