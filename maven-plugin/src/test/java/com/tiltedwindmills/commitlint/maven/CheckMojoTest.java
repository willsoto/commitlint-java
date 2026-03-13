package com.tiltedwindmills.commitlint.maven;

import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CheckMojoTest {

  @TempDir Path tempDir;

  @Test
  void validConventionalCommitPasses() throws Exception {
    final var mojo = new CheckMojo();
    final var config = mojo.buildConfig();
    final var outcome = mojo.lint(writeMessage("feat: add new feature"), config);

    Assertions.assertTrue(outcome.valid());
    Assertions.assertTrue(outcome.errors().isEmpty());
    Assertions.assertTrue(outcome.warnings().isEmpty());
  }

  @Test
  void invalidCommitFails() throws Exception {
    final var mojo = new CheckMojo();
    final var config = mojo.buildConfig();
    final var outcome = mojo.lint(writeMessage("bad message"), config);

    Assertions.assertFalse(outcome.valid());
    Assertions.assertFalse(outcome.errors().isEmpty());
  }

  @Test
  void mergeCommitIsIgnored() throws Exception {
    final var mojo = new CheckMojo();
    final var config = mojo.buildConfig();
    final var outcome = mojo.lint(writeMessage("Merge branch 'main'"), config);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void commitWithBodyAndFooterPasses() throws Exception {
    final var mojo = new CheckMojo();
    final var config = mojo.buildConfig();
    final var outcome =
        mojo.lint(
            writeMessage("fix(parser): handle edge case\n\nDetailed body.\n\nRefs: #42"), config);

    Assertions.assertTrue(outcome.valid());
    Assertions.assertTrue(outcome.errors().isEmpty());
  }

  @Test
  void missingFileThrowsMojoFailureException() throws Exception {
    final var mojo = new CheckMojo();
    final var config = mojo.buildConfig();
    final Path missing = tempDir.resolve("nonexistent");

    final var exception =
        Assertions.assertThrows(MojoFailureException.class, () -> mojo.lint(missing, config));

    Assertions.assertTrue(exception.getMessage().contains("Failed to read commit message file"));
  }

  private Path writeMessage(final String message) throws Exception {
    final Path file = tempDir.resolve("COMMIT_EDITMSG");
    Files.writeString(file, message);
    return file;
  }
}
