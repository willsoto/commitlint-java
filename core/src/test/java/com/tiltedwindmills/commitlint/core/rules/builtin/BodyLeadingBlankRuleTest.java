package com.tiltedwindmills.commitlint.core.rules.builtin;

import com.tiltedwindmills.commitlint.core.parser.CommitMessage;
import com.tiltedwindmills.commitlint.core.parser.CommitParser;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.RuleOutcome;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BodyLeadingBlankRuleTest {

  private final BodyLeadingBlankRule rule = new BodyLeadingBlankRule();
  private final CommitParser parser = new CommitParser();

  @Test
  void passesWhenNoBody() {
    final CommitMessage msg = parser.parse("feat: add feature");
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, null);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void passesWhenBodyHasLeadingBlank() {
    final CommitMessage msg = parser.parse("feat: add feature\n\nBody text here.");
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, null);

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void failsWhenBodyHasNoLeadingBlank() {
    final CommitMessage msg = parser.parse("feat: add feature\nBody text here.");
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, null);

    Assertions.assertFalse(outcome.valid());
  }

  @Test
  void neverConditionFailsWhenBodyHasLeadingBlank() {
    final CommitMessage msg = parser.parse("feat: add feature\n\nBody text here.");
    final RuleOutcome outcome = rule.validate(msg, Condition.NEVER, null);

    Assertions.assertFalse(outcome.valid());
    Assertions.assertNotNull(outcome.message());
    Assertions.assertTrue(outcome.message().contains("must not"));
  }

  @Test
  void passesWhenBodyIsEmptyString() {
    final CommitMessage msg = parser.parse("feat: add feature\n");
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, null);

    Assertions.assertTrue(outcome.valid());
  }
}
