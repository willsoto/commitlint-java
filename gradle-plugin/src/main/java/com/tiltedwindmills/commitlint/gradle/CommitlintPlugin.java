package com.tiltedwindmills.commitlint.gradle;

import java.util.HashMap;
import java.util.Map;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;

/**
 * Gradle plugin that registers a {@code commitlintCheck} task for linting commit messages against
 * Conventional Commits rules.
 *
 * <p>Apply this plugin to your build and configure it via the {@code commitlint} extension. The
 * plugin automatically wires the extension properties into the task inputs.
 */
public final class CommitlintPlugin implements Plugin<Project> {

  @Override
  public void apply(final Project project) {
    final CommitlintExtension extension =
        project.getExtensions().create("commitlint", CommitlintExtension.class);

    extension
        .getCommitMessageFile()
        .convention(project.getLayout().getProjectDirectory().file(".git/COMMIT_EDITMSG"));
    extension.getFailOnWarning().convention(true);
    extension.getDefaultIgnores().convention(true);

    final Provider<Map<String, Map<String, String>>> ruleOverrides =
        project.provider(
            () -> {
              final Map<String, Map<String, String>> result = new HashMap<>();
              for (final RuleConfiguration rule : extension.getRules()) {
                final Map<String, String> props = new HashMap<>();
                if (rule.getSeverity().isPresent()) {
                  props.put("severity", rule.getSeverity().get());
                }
                if (rule.getCondition().isPresent()) {
                  props.put("condition", rule.getCondition().get());
                }
                if (rule.getValue().isPresent()) {
                  props.put("value", rule.getValue().get());
                }
                result.put(rule.getName(), props);
              }
              return result;
            });

    project
        .getTasks()
        .register(
            "commitlintCheck",
            CommitlintCheckTask.class,
            task -> {
              task.setDescription("Lint a commit message against Conventional Commits rules.");
              task.setGroup("verification");
              task.getCommitMessageFile().set(extension.getCommitMessageFile());
              task.getFailOnWarning().set(extension.getFailOnWarning());
              task.getDefaultIgnores().set(extension.getDefaultIgnores());
              task.getRuleOverrides().set(ruleOverrides);
            });
  }
}
