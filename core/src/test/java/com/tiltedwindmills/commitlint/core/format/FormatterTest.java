package com.tiltedwindmills.commitlint.core.format;

import com.tiltedwindmills.commitlint.core.lint.LintOutcome;
import com.tiltedwindmills.commitlint.core.lint.LintRuleOutcome;
import com.tiltedwindmills.commitlint.core.rules.Severity;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FormatterTest {

  private final Formatter formatter = new Formatter();

  @Test
  void formatsValidOutcome() {
    final LintOutcome outcome = new LintOutcome("feat: ok", true, List.of(), List.of());
    final String result = formatter.format(outcome);

    Assertions.assertTrue(result.contains("\u2714"));
    Assertions.assertTrue(result.contains("0 error(s), 0 warning(s)"));
  }

  @Test
  void formatsOutcomeWithErrors() {
    final LintRuleOutcome error =
        new LintRuleOutcome(false, Severity.ERROR, "type-empty", "type must not be empty");
    final LintOutcome outcome = new LintOutcome("bad message", false, List.of(error), List.of());
    final String result = formatter.format(outcome);

    Assertions.assertTrue(result.contains("\u2716"));
    Assertions.assertTrue(result.contains("type must not be empty"));
    Assertions.assertTrue(result.contains("[type-empty]"));
    Assertions.assertTrue(result.contains("1 error(s), 0 warning(s)"));
  }

  @Test
  void formatsOutcomeWithWarnings() {
    final LintRuleOutcome warning =
        new LintRuleOutcome(
            false, Severity.WARNING, "body-leading-blank", "body must have a leading blank line");
    final LintOutcome outcome = new LintOutcome("feat: ok", true, List.of(), List.of(warning));
    final String result = formatter.format(outcome);

    Assertions.assertTrue(result.contains("\u26A0"));
    Assertions.assertTrue(result.contains("0 error(s), 1 warning(s)"));
  }
}
