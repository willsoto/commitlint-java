package com.tiltedwindmills.commitlint.core.lint;

import com.tiltedwindmills.commitlint.core.config.CommitlintConfig;
import com.tiltedwindmills.commitlint.core.config.DefaultConfig;
import com.tiltedwindmills.commitlint.core.parser.ParserOptions;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.RuleConfig;
import com.tiltedwindmills.commitlint.core.rules.RuleRegistry;
import com.tiltedwindmills.commitlint.core.rules.Severity;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LinterTest {

  private final RuleRegistry registry = RuleRegistry.withBuiltins();
  private final Linter linter = new Linter(registry);
  private final CommitlintConfig config = DefaultConfig.conventional();

  @Test
  void validConventionalCommitPasses() {
    final LintOutcome outcome = linter.lint("feat: add new feature", config);

    Assertions.assertTrue(outcome.valid());
    Assertions.assertTrue(outcome.errors().isEmpty());
  }

  @Test
  void missingTypeProducesError() {
    final LintOutcome outcome = linter.lint("just a message", config);

    Assertions.assertFalse(outcome.valid());
    Assertions.assertFalse(outcome.errors().isEmpty());
  }

  @Test
  void invalidTypeProducesError() {
    final LintOutcome outcome = linter.lint("invalid: something", config);

    Assertions.assertFalse(outcome.valid());
    Assertions.assertTrue(outcome.errors().stream().anyMatch(e -> e.name().equals("type-enum")));
  }

  @Test
  void headerTooLongProducesError() {
    final String longMsg = "feat: " + "a".repeat(200);
    final LintOutcome outcome = linter.lint(longMsg, config);

    Assertions.assertFalse(outcome.valid());
    Assertions.assertTrue(
        outcome.errors().stream().anyMatch(e -> e.name().equals("header-max-length")));
  }

  @Test
  void mergeCommitIsIgnored() {
    final LintOutcome outcome = linter.lint("Merge branch 'main'", config);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void commitWithScopeAndBodyPasses() {
    final LintOutcome outcome =
        linter.lint("fix(parser): handle edge case\n\nDetailed body.", config);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void bodyWithoutLeadingBlankProducesWarning() {
    final LintOutcome outcome = linter.lint("feat: add feature\nbody without blank", config);

    Assertions.assertTrue(
        outcome.warnings().stream().anyMatch(w -> w.name().equals("body-leading-blank")));
  }

  @Test
  void revertCommitIsIgnored() {
    final LintOutcome outcome = linter.lint("Revert \"feat: something\"", config);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void amendCommitIsIgnored() {
    final LintOutcome outcome = linter.lint("Amend previous commit", config);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void customIgnoreMatchesCommit() {
    final CommitlintConfig customConfig =
        new CommitlintConfig(
            config.rules(), ParserOptions.defaults(), List.of(raw -> raw.startsWith("WIP")), false);
    final LintOutcome outcome = linter.lint("WIP: work in progress", customConfig);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void customIgnoreThatDoesNotMatch() {
    final CommitlintConfig customConfig =
        new CommitlintConfig(
            config.rules(), ParserOptions.defaults(), List.of(raw -> raw.startsWith("WIP")), false);
    final LintOutcome outcome = linter.lint("feat: not a WIP", customConfig);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void disabledRuleIsSkipped() {
    final CommitlintConfig customConfig =
        new CommitlintConfig(
            Map.of("type-empty", RuleConfig.of(Severity.DISABLED, Condition.NEVER)),
            ParserOptions.defaults(),
            List.of(),
            true);
    final LintOutcome outcome = linter.lint("no type here", customConfig);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void unknownRuleIsSkipped() {
    final CommitlintConfig customConfig =
        new CommitlintConfig(
            Map.of("nonexistent-rule", RuleConfig.of(Severity.ERROR, Condition.ALWAYS, 10)),
            ParserOptions.defaults(),
            List.of(),
            true);
    final LintOutcome outcome = linter.lint("feat: something", customConfig);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void emptyInputIsNotDefaultIgnored() {
    final LintOutcome outcome = linter.lint("", config);

    Assertions.assertFalse(outcome.valid());
  }
}
