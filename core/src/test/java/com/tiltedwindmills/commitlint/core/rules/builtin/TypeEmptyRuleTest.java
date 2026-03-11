package com.tiltedwindmills.commitlint.core.rules.builtin;

import com.tiltedwindmills.commitlint.core.parser.CommitMessage;
import com.tiltedwindmills.commitlint.core.parser.CommitParser;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.RuleOutcome;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TypeEmptyRuleTest {

  private final TypeEmptyRule rule = new TypeEmptyRule();
  private final CommitParser parser = new CommitParser();

  @Test
  void passesWhenTypePresent_conditionNever() {
    final CommitMessage msg = parser.parse("feat: something");
    final RuleOutcome outcome = rule.validate(msg, Condition.NEVER, null);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void failsWhenTypeEmpty_conditionNever() {
    final CommitMessage msg = parser.parse("just a message");
    final RuleOutcome outcome = rule.validate(msg, Condition.NEVER, null);

    Assertions.assertFalse(outcome.valid());
  }

  @Test
  void passesWhenTypeEmpty_conditionAlways() {
    final CommitMessage msg = parser.parse("just a message");
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, null);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void failsWhenTypePresent_conditionAlways() {
    final CommitMessage msg = parser.parse("feat: something");
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, null);

    Assertions.assertFalse(outcome.valid());
    Assertions.assertNotNull(outcome.message());
    Assertions.assertTrue(outcome.message().contains("type must be empty"));
  }
}
