package com.tiltedwindmills.commitlint.cli;

import com.tiltedwindmills.commitlint.core.config.DefaultConfig;
import com.tiltedwindmills.commitlint.core.format.Formatter;
import com.tiltedwindmills.commitlint.core.lint.LintOutcome;
import com.tiltedwindmills.commitlint.core.lint.Linter;
import com.tiltedwindmills.commitlint.core.rules.RuleRegistry;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Main {

  private Main() {}

  static void main(final String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println("Usage: commitlint <commit-message-file>");
      System.exit(1);
    }

    final boolean valid = run(Path.of(args[0]), System.out);
    System.exit(valid ? 0 : 1);
  }

  static boolean run(final Path file, final PrintStream out) throws IOException {
    final String message = Files.readString(file).trim();

    final var registry = RuleRegistry.withBuiltins();
    final var linter = new Linter(registry);
    final var config = DefaultConfig.conventional();

    final LintOutcome outcome = linter.lint(message, config);

    out.println(new Formatter().format(outcome));

    return outcome.valid();
  }
}
