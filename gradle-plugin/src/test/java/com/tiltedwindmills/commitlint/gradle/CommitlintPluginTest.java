package com.tiltedwindmills.commitlint.gradle;

import java.util.Map;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommitlintPluginTest {

  @Test
  void pluginAppliesWithoutError() {
    final Project project = ProjectBuilder.builder().build();

    Assertions.assertDoesNotThrow(() -> project.getPlugins().apply(CommitlintPlugin.class));
  }

  @Test
  void extensionCreatedWithConventionDefaults() {
    final Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply(CommitlintPlugin.class);
    final var extension = project.getExtensions().getByType(CommitlintExtension.class);

    Assertions.assertTrue(extension.getFailOnWarning().get());
    Assertions.assertTrue(extension.getDefaultIgnores().get());
    Assertions.assertTrue(
        extension.getCommitMessageFile().get().getAsFile().getPath().endsWith("COMMIT_EDITMSG"));
  }

  @Test
  void commitlintCheckTaskRegistered() {
    final Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply(CommitlintPlugin.class);

    final var task = project.getTasks().findByName("commitlintCheck");

    Assertions.assertNotNull(task);
    Assertions.assertEquals("verification", task.getGroup());
  }

  @Test
  void taskPropertiesWiredFromExtension() {
    final Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply(CommitlintPlugin.class);
    final var extension = project.getExtensions().getByType(CommitlintExtension.class);
    extension.getFailOnWarning().set(false);

    final var task = (CommitlintCheckTask) project.getTasks().getByName("commitlintCheck");

    Assertions.assertFalse(task.getFailOnWarning().get());
  }

  @Test
  void extensionRulesContainerIsAccessible() {
    final Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply(CommitlintPlugin.class);
    final var extension = project.getExtensions().getByType(CommitlintExtension.class);

    Assertions.assertNotNull(extension.getRules());
    Assertions.assertTrue(extension.getRules().isEmpty());
  }

  @Test
  void ruleOverridesWiredFromExtensionWithAllFields() {
    final Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply(CommitlintPlugin.class);
    final var extension = project.getExtensions().getByType(CommitlintExtension.class);
    extension.rules(
        rules -> {
          rules.create(
              "header-max-length",
              rule -> {
                rule.getSeverity().set("WARNING");
                rule.getCondition().set("ALWAYS");
                rule.getValue().set("72");
              });
        });

    final var task = (CommitlintCheckTask) project.getTasks().getByName("commitlintCheck");
    final Map<String, Map<String, String>> overrides = task.getRuleOverrides().get();

    Assertions.assertEquals(1, overrides.size());
    Assertions.assertEquals("WARNING", overrides.get("header-max-length").get("severity"));
    Assertions.assertEquals("ALWAYS", overrides.get("header-max-length").get("condition"));
    Assertions.assertEquals("72", overrides.get("header-max-length").get("value"));
  }

  @Test
  void ruleOverridesWithSeverityOnly() {
    final Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply(CommitlintPlugin.class);
    final var extension = project.getExtensions().getByType(CommitlintExtension.class);
    extension.rules(
        rules -> {
          rules.create(
              "type-empty",
              rule -> {
                rule.getSeverity().set("DISABLED");
              });
        });

    final var task = (CommitlintCheckTask) project.getTasks().getByName("commitlintCheck");
    final Map<String, Map<String, String>> overrides = task.getRuleOverrides().get();

    Assertions.assertEquals(1, overrides.size());
    Assertions.assertEquals("DISABLED", overrides.get("type-empty").get("severity"));
    Assertions.assertFalse(overrides.get("type-empty").containsKey("condition"));
    Assertions.assertFalse(overrides.get("type-empty").containsKey("value"));
  }

  @Test
  void ruleOverridesWithConditionOnly() {
    final Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply(CommitlintPlugin.class);
    final var extension = project.getExtensions().getByType(CommitlintExtension.class);
    extension.rules(
        rules -> {
          rules.create(
              "type-empty",
              rule -> {
                rule.getCondition().set("ALWAYS");
              });
        });

    final var task = (CommitlintCheckTask) project.getTasks().getByName("commitlintCheck");
    final Map<String, Map<String, String>> overrides = task.getRuleOverrides().get();

    Assertions.assertEquals("ALWAYS", overrides.get("type-empty").get("condition"));
    Assertions.assertFalse(overrides.get("type-empty").containsKey("severity"));
    Assertions.assertFalse(overrides.get("type-empty").containsKey("value"));
  }

  @Test
  void ruleOverridesWithValueOnly() {
    final Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply(CommitlintPlugin.class);
    final var extension = project.getExtensions().getByType(CommitlintExtension.class);
    extension.rules(
        rules -> {
          rules.create(
              "header-max-length",
              rule -> {
                rule.getValue().set("72");
              });
        });

    final var task = (CommitlintCheckTask) project.getTasks().getByName("commitlintCheck");
    final Map<String, Map<String, String>> overrides = task.getRuleOverrides().get();

    Assertions.assertEquals("72", overrides.get("header-max-length").get("value"));
    Assertions.assertFalse(overrides.get("header-max-length").containsKey("severity"));
    Assertions.assertFalse(overrides.get("header-max-length").containsKey("condition"));
  }

  @Test
  void emptyRulesProducesEmptyOverrides() {
    final Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply(CommitlintPlugin.class);

    final var task = (CommitlintCheckTask) project.getTasks().getByName("commitlintCheck");
    final Map<String, Map<String, String>> overrides = task.getRuleOverrides().get();

    Assertions.assertTrue(overrides.isEmpty());
  }
}
