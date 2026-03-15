package com.tiltedwindmills.commitlint.maven;

import com.tiltedwindmills.commitlint.core.config.CommitlintConfig;
import com.tiltedwindmills.commitlint.core.config.ConfigBuilder;
import com.tiltedwindmills.commitlint.core.config.RuleOverride;
import com.tiltedwindmills.commitlint.core.format.Formatter;
import com.tiltedwindmills.commitlint.core.lint.LintOutcome;
import com.tiltedwindmills.commitlint.core.lint.Linter;
import com.tiltedwindmills.commitlint.core.rules.RuleRegistry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/** Lints a commit message file against Conventional Commits rules. */
@Mojo(name = "check", requiresProject = false)
public final class CheckMojo extends AbstractMojo {

  @Parameter(
      property = "commitMessageFile",
      defaultValue = "${project.basedir}/.git/COMMIT_EDITMSG")
  private String commitMessageFile;

  @Parameter(property = "failOnWarning", defaultValue = "true")
  private boolean failOnWarning;

  @Parameter(property = "defaultIgnores", defaultValue = "true")
  private boolean defaultIgnores = true;

  @Parameter private Map<String, RuleOverride> rules;

  @Override
  public void execute() throws MojoFailureException {
    final Path file = Path.of(commitMessageFile);
    final CommitlintConfig config = buildConfig();
    final LintOutcome outcome = lint(file, config);
    final String formatted = new Formatter().format(outcome);
    getLog().info(formatted);

    if (!outcome.valid()) {
      throw new MojoFailureException("Commit message lint failed:\n" + formatted);
    }
    if (failOnWarning && !outcome.warnings().isEmpty()) {
      throw new MojoFailureException("Commit message lint has warnings:\n" + formatted);
    }
  }

  CommitlintConfig buildConfig() throws MojoFailureException {
    try {
      final Map<String, RuleOverride> effectiveRules = rules != null ? rules : Map.of();
      return ConfigBuilder.build(effectiveRules, defaultIgnores);
    } catch (final IllegalArgumentException e) {
      throw new MojoFailureException(e.getMessage(), e);
    }
  }

  LintOutcome lint(final Path file, final CommitlintConfig config) throws MojoFailureException {
    try {
      final String message = Files.readString(file).trim();
      final var registry = RuleRegistry.withBuiltins();
      final var linter = new Linter(registry);
      return linter.lint(message, config);
    } catch (final IOException e) {
      throw new MojoFailureException("Failed to read commit message file: " + file, e);
    }
  }
}
