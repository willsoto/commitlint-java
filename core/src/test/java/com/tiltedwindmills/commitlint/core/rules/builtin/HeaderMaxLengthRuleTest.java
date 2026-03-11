package com.tiltedwindmills.commitlint.core.rules.builtin;

import com.tiltedwindmills.commitlint.core.parser.CommitMessage;
import com.tiltedwindmills.commitlint.core.parser.CommitParser;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.RuleOutcome;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HeaderMaxLengthRuleTest {

  private final HeaderMaxLengthRule rule = new HeaderMaxLengthRule();
  private final CommitParser parser = new CommitParser();

  @Test
  void passesWhenHeaderWithinLimit() {
    final CommitMessage msg = parser.parse("feat: short");
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, 100);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void failsWhenHeaderExceedsLimit() {
    final CommitMessage msg = parser.parse("feat: " + "a".repeat(100));
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, 10);

    Assertions.assertFalse(outcome.valid());
    Assertions.assertNotNull(outcome.message());
  }

  @Test
  void passesAtExactLimit() {
    final CommitMessage msg = parser.parse("feat: ok");
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, msg.header().length());

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void neverConditionFailsWithinLimit() {
    final CommitMessage msg = parser.parse("feat: short");
    final RuleOutcome outcome = rule.validate(msg, Condition.NEVER, 100);

    Assertions.assertFalse(outcome.valid());
    Assertions.assertNotNull(outcome.message());
    Assertions.assertTrue(outcome.message().contains("must not"));
  }
}
