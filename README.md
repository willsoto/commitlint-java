# commitlint-java

A Java port of [commitlint](https://commitlint.js.org/) — lint commit messages against the [Conventional Commits](https://www.conventionalcommits.org/) format.

## Modules

- **core** — Parser, rules engine, linter, and formatter. Zero runtime dependencies.
- **cli** — Command-line entrypoint for linting commit message files. Used by the `commit-msg` git hook.

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

## Usage

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
core/src/main/java/com/tiltedwindmills/commitlint/core/
├── parser/          # Commit message parsing
├── rules/           # Rule interface, registry, and built-in rules
├── ensure/          # Validation utilities
├── config/          # Configuration loading
├── lint/            # Linter pipeline
└── format/          # Output formatting
```

## License

[Apache License 2.0](LICENSE)
