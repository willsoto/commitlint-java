package com.tiltedwindmills.commitlint.maven;

import com.tiltedwindmills.commitlint.core.config.RuleOverride;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.apache.maven.plugin.MojoFailureException;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CheckMojoExecuteTest {

  @TempDir Path tempDir;

  @Test
  void validCommitDoesNotThrow() throws Exception {
    final var mojo = createMojo(writeMessage("feat: add new feature"), false);

    Assertions.assertDoesNotThrow(mojo::execute);
  }

  @Test
  void invalidCommitThrowsWithLintFailed() throws Exception {
    final var mojo = createMojo(writeMessage("bad message"), false);

    final var exception = Assertions.assertThrows(MojoFailureException.class, mojo::execute);

    Assertions.assertTrue(exception.getMessage().contains("lint failed"));
  }

  @Test
  void failOnWarningTrueWithWarningThrows() throws Exception {
    final var mojo = createMojo(writeMessage("feat: add feature\nBody without blank."), true);

    final var exception = Assertions.assertThrows(MojoFailureException.class, mojo::execute);

    Assertions.assertTrue(exception.getMessage().contains("has warnings"));
  }

  @Test
  void failOnWarningFalseWithWarningDoesNotThrow() throws Exception {
    final var mojo = createMojo(writeMessage("feat: add feature\nBody without blank."), false);

    Assertions.assertDoesNotThrow(mojo::execute);
  }

  @Test
  void missingFileThrowsWithIoExceptionCause() throws Exception {
    final var mojo = createMojo(tempDir.resolve("nonexistent").toString(), false);

    final var exception = Assertions.assertThrows(MojoFailureException.class, mojo::execute);

    Assertions.assertInstanceOf(IOException.class, exception.getCause());
  }

  @Test
  void failOnWarningTrueWithNoWarningsDoesNotThrow() throws Exception {
    final var mojo = createMojo(writeMessage("feat: add new feature"), true);

    Assertions.assertDoesNotThrow(mojo::execute);
  }

  @Test
  void mergeCommitDoesNotThrow() throws Exception {
    final var mojo = createMojo(writeMessage("Merge branch 'main'"), false);

    Assertions.assertDoesNotThrow(mojo::execute);
  }

  @Test
  void executeWithDisabledRulePassesInvalidCommit() throws Exception {
    final var mojo =
        createMojoWithRules(
            writeMessage("bad message"),
            false,
            Map.of("type-empty", overrideWith("DISABLED", null, null)));

    Assertions.assertDoesNotThrow(mojo::execute);
  }

  @Test
  void executeWithCustomTypeEnumRejectsUnlistedType() throws Exception {
    final var mojo =
        createMojoWithRules(
            writeMessage("feat: something"),
            false,
            Map.of("type-enum", overrideWith(null, null, "fix,chore")));

    final var exception = Assertions.assertThrows(MojoFailureException.class, mojo::execute);

    Assertions.assertTrue(exception.getMessage().contains("lint failed"));
  }

  @Test
  void executeWithUnknownRuleThrows() throws Exception {
    final var mojo =
        createMojoWithRules(
            writeMessage("feat: something"),
            false,
            Map.of("nonexistent-rule", overrideWith("ERROR", null, null)));

    final var exception = Assertions.assertThrows(MojoFailureException.class, mojo::execute);

    Assertions.assertTrue(exception.getMessage().contains("nonexistent-rule"));
  }

  private CheckMojo createMojo(final String commitMessageFile, final boolean failOnWarning)
      throws Exception {
    final var mojo = new CheckMojo();
    setField(mojo, "commitMessageFile", commitMessageFile);
    setField(mojo, "failOnWarning", failOnWarning);
    return mojo;
  }

  private CheckMojo createMojoWithRules(
      final String commitMessageFile,
      final boolean failOnWarning,
      final Map<String, RuleOverride> rules)
      throws Exception {
    final var mojo = createMojo(commitMessageFile, failOnWarning);
    setField(mojo, "rules", rules);
    return mojo;
  }

  private static RuleOverride overrideWith(
      final @Nullable String severity,
      final @Nullable String condition,
      final @Nullable String value) {
    final var override = new RuleOverride();
    if (severity != null) {
      override.setSeverity(severity);
    }
    if (condition != null) {
      override.setCondition(condition);
    }
    if (value != null) {
      override.setValue(value);
    }
    return override;
  }

  private static void setField(final Object target, final String fieldName, final Object value)
      throws Exception {
    final Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private String writeMessage(final String message) throws Exception {
    final Path file = tempDir.resolve("COMMIT_EDITMSG");
    Files.writeString(file, message);
    return file.toString();
  }
}
