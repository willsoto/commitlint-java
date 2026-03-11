package com.tiltedwindmills.commitlint.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MainTest {

  @TempDir Path tempDir;

  @Test
  void validConventionalCommitPasses() throws Exception {
    final var result = run("feat: add new feature");

    Assertions.assertTrue(result.valid());
    Assertions.assertTrue(result.output().contains("\u2714"));
    Assertions.assertTrue(result.output().contains("0 error(s), 0 warning(s)"));
  }

  @Test
  void invalidCommitFails() throws Exception {
    final var result = run("bad message");

    Assertions.assertFalse(result.valid());
    Assertions.assertTrue(result.output().contains("\u2716"));
    Assertions.assertTrue(result.output().contains("type must not be empty"));
  }

  @Test
  void invalidTypeFails() throws Exception {
    final var result = run("invalid: something");

    Assertions.assertFalse(result.valid());
    Assertions.assertTrue(result.output().contains("type must be one of"));
  }

  @Test
  void mergeCommitIsIgnored() throws Exception {
    final var result = run("Merge branch 'main'");

    Assertions.assertTrue(result.valid());
  }

  @Test
  void commitWithBodyAndFooterPasses() throws Exception {
    final var result = run("fix(parser): handle edge case\n\nDetailed body.\n\nRefs: #42");

    Assertions.assertTrue(result.valid());
    Assertions.assertTrue(result.output().contains("0 error(s), 0 warning(s)"));
  }

  private RunResult run(final String message) throws Exception {
    final Path file = tempDir.resolve("COMMIT_EDITMSG");
    Files.writeString(file, message);

    final var out = new ByteArrayOutputStream();
    final boolean valid = Main.run(file, new PrintStream(out, true, StandardCharsets.UTF_8));
    return new RunResult(valid, out.toString(StandardCharsets.UTF_8));
  }

  private record RunResult(boolean valid, String output) {}
}
