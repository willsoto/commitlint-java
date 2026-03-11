package com.tiltedwindmills.commitlint.core.rules.builtin;

import com.tiltedwindmills.commitlint.core.parser.CommitMessage;
import com.tiltedwindmills.commitlint.core.parser.CommitParser;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.RuleOutcome;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TypeEnumRuleTest {

  private final TypeEnumRule rule = new TypeEnumRule();
  private final CommitParser parser = new CommitParser();
  private final List<String> allowed = List.of("feat", "fix", "docs");

  @Test
  void passesWhenTypeInEnum() {
    final CommitMessage msg = parser.parse("feat: add feature");
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, allowed);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void failsWhenTypeNotInEnum() {
    final CommitMessage msg = parser.parse("chore: cleanup");
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, allowed);

    Assertions.assertFalse(outcome.valid());
  }

  @Test
  void passesWhenTypeNull() {
    final CommitMessage msg = parser.parse("not a conventional commit");
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, allowed);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void neverConditionInvertsResult() {
    final CommitMessage msg = parser.parse("feat: feature");
    final RuleOutcome outcome = rule.validate(msg, Condition.NEVER, allowed);

    Assertions.assertFalse(outcome.valid());
  }
}
