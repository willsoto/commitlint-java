package com.tiltedwindmills.commitlint.gradle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CommitlintPluginFunctionalTest {

  @TempDir Path tempDir;

  @Test
  void validCommitMessagePasses() throws IOException {
    writeBuildFile("");
    writeCommitMessage("feat: add new feature");

    final BuildResult result = runner("commitlintCheck").build();

    Assertions.assertEquals(TaskOutcome.SUCCESS, result.task(":commitlintCheck").getOutcome());
  }

  @Test
  void invalidCommitMessageFails() throws IOException {
    writeBuildFile("");
    writeCommitMessage("bad message");

    final BuildResult result = runner("commitlintCheck").buildAndFail();

    Assertions.assertEquals(TaskOutcome.FAILED, result.task(":commitlintCheck").getOutcome());
    Assertions.assertTrue(result.getOutput().contains("lint failed"));
  }

  @Test
  void customRuleOverrideApplied() throws IOException {
    writeBuildFile(
        """
        commitlint {
          rules {
            create("type-enum") {
              value.set("fix,chore")
            }
          }
        }
        """);
    writeCommitMessage("feat: something");

    final BuildResult result = runner("commitlintCheck").buildAndFail();

    Assertions.assertEquals(TaskOutcome.FAILED, result.task(":commitlintCheck").getOutcome());
  }

  @Test
  void taskListedInOutput() throws IOException {
    writeBuildFile("");

    final BuildResult result = runner("tasks", "--group=verification").build();

    Assertions.assertTrue(result.getOutput().contains("commitlintCheck"));
  }

  private void writeBuildFile(final String extraConfig) throws IOException {
    final String content =
        """
        plugins {
          id("com.tilted-windmills.commitlint")
        }
        """
            + extraConfig;
    Files.writeString(tempDir.resolve("build.gradle.kts"), content);
    Files.writeString(tempDir.resolve("settings.gradle.kts"), "");
  }

  private void writeCommitMessage(final String message) throws IOException {
    final Path gitDir = tempDir.resolve(".git");
    Files.createDirectories(gitDir);
    Files.writeString(gitDir.resolve("COMMIT_EDITMSG"), message);
  }

  private GradleRunner runner(final String... args) {
    return GradleRunner.create()
        .withProjectDir(tempDir.toFile())
        .withPluginClasspath()
        .withArguments(args);
  }
}
