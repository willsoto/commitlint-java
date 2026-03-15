package com.tiltedwindmills.commitlint.gradle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CommitlintCheckTaskTest {

  @TempDir Path tempDir;

  private Project project;
  private CommitlintCheckTask task;

  @BeforeEach
  void setUp() {
    project = ProjectBuilder.builder().withProjectDir(tempDir.toFile()).build();
    project.getPlugins().apply(CommitlintPlugin.class);
    task = (CommitlintCheckTask) project.getTasks().getByName("commitlintCheck");
  }

  @Test
  void validCommitPasses() throws Exception {
    writeMessage("feat: add new feature");

    Assertions.assertDoesNotThrow(() -> task.check());
  }

  @Test
  void invalidCommitThrowsGradleException() throws Exception {
    writeMessage("bad message");

    final var exception = Assertions.assertThrows(GradleException.class, () -> task.check());

    Assertions.assertTrue(exception.getMessage().contains("lint failed"));
  }

  @Test
  void failOnWarningTrueWithWarningsThrows() throws Exception {
    writeMessage("feat: add feature\nBody without blank.");
    task.getFailOnWarning().set(true);

    final var exception = Assertions.assertThrows(GradleException.class, () -> task.check());

    Assertions.assertTrue(exception.getMessage().contains("has warnings"));
  }

  @Test
  void failOnWarningFalseWithWarningsPasses() throws Exception {
    writeMessage("feat: add feature\nBody without blank.");
    task.getFailOnWarning().set(false);

    Assertions.assertDoesNotThrow(() -> task.check());
  }

  @Test
  void missingFileThrowsWithClearMessage() {
    task.getCommitMessageFile().set(tempDir.resolve("nonexistent").toFile());

    final var exception = Assertions.assertThrows(GradleException.class, () -> task.check());

    Assertions.assertTrue(exception.getMessage().contains("not found"));
  }

  @Test
  void customRuleOverridesApply() throws Exception {
    writeMessage("feat: something");
    task.getRuleOverrides().put("type-enum", Map.of("value", "fix,chore"));

    final var exception = Assertions.assertThrows(GradleException.class, () -> task.check());

    Assertions.assertTrue(exception.getMessage().contains("lint failed"));
  }

  @Test
  void mergeCommitIgnoredWithDefaultIgnores() throws Exception {
    writeMessage("Merge branch 'main'");
    task.getDefaultIgnores().set(true);

    Assertions.assertDoesNotThrow(() -> task.check());
  }

  @Test
  void overrideWithSeverityAndCondition() throws Exception {
    writeMessage("feat: add feature");
    task.getRuleOverrides()
        .put(
            "header-max-length",
            Map.of("severity", "WARNING", "condition", "ALWAYS", "value", "72"));

    Assertions.assertDoesNotThrow(() -> task.check());
  }

  @Test
  void unreadableFileThrowsGradleException() throws Exception {
    final Path dir = tempDir.resolve(".git").resolve("COMMIT_EDITMSG");
    Files.createDirectories(dir);
    Files.createDirectory(dir.resolve("subdir"));
    task.getCommitMessageFile().set(dir.toFile());

    Assertions.assertThrows(GradleException.class, () -> task.check());
  }

  @Test
  void emptyOverridesMapPasses() throws Exception {
    writeMessage("feat: add feature");
    task.getRuleOverrides().set(Map.of());

    Assertions.assertDoesNotThrow(() -> task.check());
  }

  private void writeMessage(final String message) throws Exception {
    final Path file = tempDir.resolve(".git").resolve("COMMIT_EDITMSG");
    Files.createDirectories(file.getParent());
    Files.writeString(file, message);
  }
}
