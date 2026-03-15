package com.tiltedwindmills.commitlint.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommitlintExtensionTest {

  private CommitlintExtension extension;

  @BeforeEach
  void setUp() {
    final Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply(CommitlintPlugin.class);
    extension = project.getExtensions().getByType(CommitlintExtension.class);
  }

  @Test
  void settingCommitMessageFile() {
    final Project project = ProjectBuilder.builder().build();
    project.getPlugins().apply(CommitlintPlugin.class);
    final var ext = project.getExtensions().getByType(CommitlintExtension.class);
    ext.getCommitMessageFile().set(project.getLayout().getProjectDirectory().file("custom.txt"));

    Assertions.assertTrue(
        ext.getCommitMessageFile().get().getAsFile().getPath().endsWith("custom.txt"));
  }

  @Test
  void settingFailOnWarning() {
    extension.getFailOnWarning().set(false);

    Assertions.assertFalse(extension.getFailOnWarning().get());
  }

  @Test
  void settingDefaultIgnores() {
    extension.getDefaultIgnores().set(false);

    Assertions.assertFalse(extension.getDefaultIgnores().get());
  }

  @Test
  void addingRuleConfigurations() {
    extension.rules(
        rules -> {
          rules.create(
              "header-max-length",
              rule -> {
                rule.getSeverity().set("WARNING");
                rule.getValue().set("72");
              });
        });

    final var rule = extension.getRules().getByName("header-max-length");
    Assertions.assertEquals("WARNING", rule.getSeverity().get());
    Assertions.assertEquals("72", rule.getValue().get());
    Assertions.assertFalse(rule.getCondition().isPresent());
  }
}
