# commitlint-java

A Java port of [commitlint](https://commitlint.js.org/) — lint commit messages against the [Conventional Commits](https://www.conventionalcommits.org/) format.

## Modules

- **core** — Parser, rules engine, linter, and formatter.
- **cli** — Command-line entrypoint for linting commit message files. Used by the `commit-msg` git hook.
- **maven-plugin** — Maven plugin that lints commit messages during a build. Provides a `check` goal.
- **gradle-plugin** — Gradle plugin that lints commit messages during a build. Provides a `commitlintCheck` task.

## Installation

### Core Library (Maven)

<!-- x-release-please-start-version -->

```xml
<dependency>
  <groupId>com.tilted-windmills.commitlint</groupId>
  <artifactId>commitlint-core</artifactId>
  <version>1.0.3</version>
</dependency>
```

<!-- x-release-please-end -->

### Core Library (Gradle)

<!-- x-release-please-start-version -->

```kotlin
implementation("com.tilted-windmills.commitlint:commitlint-core:1.0.3")
```

<!-- x-release-please-end -->

### Maven Plugin

<!-- x-release-please-start-version -->

```xml
<plugin>
  <groupId>com.tilted-windmills.commitlint</groupId>
  <artifactId>commitlint-maven-plugin</artifactId>
  <version>1.0.3</version>
</plugin>
```

<!-- x-release-please-end -->

### Gradle Plugin

<!-- x-release-please-start-version -->

```kotlin
plugins {
    id("com.tilted-windmills.commitlint") version "1.0.3"
}
```

<!-- x-release-please-end -->

## Requirements

- Java 25+
- Gradle 9.4+

If you use [SDKMAN](https://sdkman.io/), the `.sdkmanrc` file will pin the correct versions:

```sh
sdk env install
```

## Gradle Commands

|              Command               |                                           Description                                           |
|------------------------------------|-------------------------------------------------------------------------------------------------|
| `./gradlew build`                  | Compile, run tests, and check formatting                                                        |
| `./gradlew test`                   | Run all tests                                                                                   |
| `./gradlew spotlessCheck`          | Check formatting without modifying files                                                        |
| `./gradlew spotlessApply`          | Auto-fix formatting (google-java-format for Java, flexmark for Markdown, ktlint for Gradle KTS) |
| `./gradlew :cli:run --args=<file>` | Lint a commit message file via the CLI                                                          |
| `./gradlew jacocoTestReport`       | Generate HTML coverage reports under each module's `build/reports/jacoco/`                      |
| `./gradlew clean`                  | Delete all build outputs                                                                        |
| `./gradlew :core:test`             | Run tests for `core` only                                                                       |

## CLI

Lint a commit message file from the command line:

```sh
./gradlew :cli:run --args=".git/COMMIT_EDITMSG"
```

Exits with code `0` if the message is valid, `1` otherwise.

### Git Hook (CLI)

The CLI is designed to be used as a `commit-msg` git hook. With [Lefthook](https://github.com/evilmartians/lefthook):

```yaml
commit-msg:
  commands:
    commitlint:
      run: ./gradlew :cli:run --args="{1}"
```

## Core Library

Use the core module directly for programmatic access:

```java
import com.tiltedwindmills.commitlint.core.config.DefaultConfig;
import com.tiltedwindmills.commitlint.core.format.Formatter;
import com.tiltedwindmills.commitlint.core.lint.LintOutcome;
import com.tiltedwindmills.commitlint.core.lint.Linter;
import com.tiltedwindmills.commitlint.core.rules.RuleRegistry;

var registry = RuleRegistry.withBuiltins();
var linter = new Linter(registry);
var config = DefaultConfig.conventional();

LintOutcome outcome = linter.lint("feat(parser): add scope support", config);

System.out.println(new Formatter().format(outcome));
// ✔ input: feat(parser): add scope support
//
// 0 error(s), 0 warning(s)
```

## Maven Plugin

Add the plugin to your `pom.xml`:

<!-- x-release-please-start-version -->

```xml
<plugin>
  <groupId>com.tilted-windmills.commitlint</groupId>
  <artifactId>commitlint-maven-plugin</artifactId>
  <version>1.0.3</version>
  <configuration>
    <!-- All parameters are optional — defaults shown below -->
    <commitMessageFile>${project.basedir}/.git/COMMIT_EDITMSG</commitMessageFile>
    <failOnWarning>true</failOnWarning>
    <defaultIgnores>true</defaultIgnores>
  </configuration>
</plugin>
```

<!-- x-release-please-end -->

Run the `check` goal:

```sh
mvn commitlint:check
```

### Git Hook (Maven)

Use the Maven plugin as a `commit-msg` git hook with [Lefthook](https://github.com/evilmartians/lefthook):

```yaml
commit-msg:
  commands:
    commitlint:
      run: mvn commitlint:check
```

### Per-Rule Configuration

Override severity, condition, or value for any built-in rule via nested `<rules>` configuration. Omitted fields keep their defaults from `DefaultConfig.conventional()`.

```xml
<configuration>
  <rules>
    <header-max-length>
      <severity>WARNING</severity>
      <value>72</value>
    </header-max-length>
    <type-enum>
      <value>feat,fix,docs,chore</value>
    </type-enum>
    <type-empty>
      <severity>DISABLED</severity>
    </type-empty>
    <subject-case>
      <condition>NEVER</condition>
      <value>UPPER_CASE,PASCAL_CASE</value>
    </subject-case>
  </rules>
</configuration>
```

Each rule element supports up to three child elements:

|    Element    |             Values             |                   Description                    |
|---------------|--------------------------------|--------------------------------------------------|
| `<severity>`  | `DISABLED`, `WARNING`, `ERROR` | Override the rule's severity level               |
| `<condition>` | `ALWAYS`, `NEVER`              | Override whether the rule is positive or negated |
| `<value>`     | _(rule-dependent)_             | Override the rule's value (see table below)      |

**Value formats by rule:**

|         Rule         |                                                                  Value format                                                                   |         Example          |
|----------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------|
| `header-max-length`  | Integer                                                                                                                                         | `72`                     |
| `type-enum`          | Comma-separated strings                                                                                                                         | `feat,fix,docs,chore`    |
| `subject-case`       | Comma-separated case types (`LOWER_CASE`, `UPPER_CASE`, `CAMEL_CASE`, `KEBAB_CASE`, `SNAKE_CASE`, `PASCAL_CASE`, `SENTENCE_CASE`, `START_CASE`) | `UPPER_CASE,PASCAL_CASE` |
| `type-empty`         | _(ignored)_                                                                                                                                     | —                        |
| `body-leading-blank` | _(ignored)_                                                                                                                                     | —                        |

## Gradle Plugin

Apply the plugin in your `build.gradle.kts`:

<!-- x-release-please-start-version -->

```kotlin
plugins {
    id("com.tilted-windmills.commitlint") version "1.0.3"
}
```

<!-- x-release-please-end -->

Run the `commitlintCheck` task:

```sh
./gradlew commitlintCheck
```

### Git Hook (Gradle)

Use the Gradle plugin as a `commit-msg` git hook with [Lefthook](https://github.com/evilmartians/lefthook):

```yaml
commit-msg:
  commands:
    commitlint:
      run: ./gradlew commitlintCheck
```

### Configuration

All parameters are optional — defaults shown below:

```kotlin
commitlint {
    commitMessageFile.set(file(".git/COMMIT_EDITMSG"))  // default
    failOnWarning.set(true)                              // default
    defaultIgnores.set(true)                             // default
}
```

### Per-Rule Configuration

Override severity, condition, or value for any built-in rule:

```kotlin
commitlint {
    rules {
        create("header-max-length") {
            severity.set("WARNING")
            value.set("72")
        }
        create("type-enum") {
            value.set("feat,fix,docs,chore")
        }
        create("type-empty") {
            severity.set("DISABLED")
        }
    }
}
```

## Built-in Rules

|         Rule         |    Value Type    |                  Description                  |
|----------------------|------------------|-----------------------------------------------|
| `header-max-length`  | `Integer`        | Header must not exceed the given length       |
| `type-empty`         | `Void`           | Type must not be empty (with `NEVER`)         |
| `type-enum`          | `List<String>`   | Type must be one of the allowed values        |
| `subject-case`       | `List<CaseType>` | Subject must (not) match the given case types |
| `body-leading-blank` | `Void`           | Body must have a leading blank line           |

## Default Configuration

`DefaultConfig.conventional()` enforces:

- **header-max-length**: 100 characters (error)
- **type-empty**: never (error)
- **type-enum**: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`, `perf`, `ci`, `build`, `revert` (error)
- **subject-case**: never `PascalCase`, `UPPER_CASE`, or `Start Case` (error)
- **body-leading-blank**: always (warning)

Merge, revert, and amend commits are ignored by default.

## Custom Rules

Implement the `Rule<V>` functional interface and register it:

```java
Rule<Integer> myRule = (message, condition, value) -> {
    boolean valid = condition.test(message.subject().length() >= value);
    return new RuleOutcome(valid, valid ? null : "subject too short");
};

var registry = RuleRegistry.withBuiltins();
registry.register("subject-min-length", myRule, Integer.class);
```

## Project Structure

```
cli/src/main/java/com/tiltedwindmills/commitlint/cli/
└── Main.java        # CLI entrypoint

core/src/main/java/com/tiltedwindmills/commitlint/core/
├── parser/          # Commit message parsing
├── rules/           # Rule interface, registry, and built-in rules
├── ensure/          # Validation utilities
├── config/          # Configuration loading
├── lint/            # Linter pipeline
└── format/          # Output formatting

maven-plugin/src/main/java/com/tiltedwindmills/commitlint/maven/
└── CheckMojo.java   # Maven plugin goal

gradle-plugin/src/main/java/com/tiltedwindmills/commitlint/gradle/
├── CommitlintPlugin.java      # Plugin entry point
├── CommitlintExtension.java   # DSL extension
├── CommitlintCheckTask.java   # Linting task
└── RuleConfiguration.java     # Per-rule config type
```

## License

[Apache License 2.0](LICENSE)
