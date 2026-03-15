package com.tiltedwindmills.commitlint.gradle;

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
import java.util.LinkedHashMap;
import java.util.Map;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.gradle.work.DisableCachingByDefault;

/** Task that lints a commit message file against Conventional Commits rules. */
@DisableCachingByDefault(because = "Commit message changes on every commit")
public abstract class CommitlintCheckTask extends DefaultTask {

  @InputFile
  @PathSensitive(PathSensitivity.NONE)
  public abstract RegularFileProperty getCommitMessageFile();

  @Input
  public abstract Property<Boolean> getFailOnWarning();

  @Input
  public abstract Property<Boolean> getDefaultIgnores();

  @Input
  public abstract MapProperty<String, Map<String, String>> getRuleOverrides();

  @TaskAction
  public void check() {
    final Path file = getCommitMessageFile().get().getAsFile().toPath();
    if (!Files.exists(file)) {
      throw new GradleException("Commit message file not found: " + file);
    }

    final Map<String, RuleOverride> overrides = convertOverrides();
    final CommitlintConfig config = ConfigBuilder.build(overrides, getDefaultIgnores().get());

    final String message;
    try {
      message = Files.readString(file).trim();
    } catch (final IOException e) {
      throw new GradleException("Failed to read commit message file: " + file, e);
    }

    final var registry = RuleRegistry.withBuiltins();
    final var linter = new Linter(registry);
    final LintOutcome outcome = linter.lint(message, config);
    final String formatted = new Formatter().format(outcome);
    getLogger().lifecycle(formatted);

    if (!outcome.valid()) {
      throw new GradleException("Commit message lint failed:\n" + formatted);
    }
    if (getFailOnWarning().get() && !outcome.warnings().isEmpty()) {
      throw new GradleException("Commit message lint has warnings:\n" + formatted);
    }
  }

  private Map<String, RuleOverride> convertOverrides() {
    final Map<String, Map<String, String>> raw = getRuleOverrides().getOrElse(Map.of());
    final Map<String, RuleOverride> result = new LinkedHashMap<>();
    for (final var entry : raw.entrySet()) {
      final var override = new RuleOverride();
      final Map<String, String> props = entry.getValue();
      if (props.containsKey("severity")) {
        override.setSeverity(props.get("severity"));
      }
      if (props.containsKey("condition")) {
        override.setCondition(props.get("condition"));
      }
      if (props.containsKey("value")) {
        override.setValue(props.get("value"));
      }
      result.put(entry.getKey(), override);
    }
    return result;
  }
}
