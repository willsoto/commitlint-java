package com.tiltedwindmills.commitlint.core.rules.builtin;

import com.tiltedwindmills.commitlint.core.parser.CommitMessage;
import com.tiltedwindmills.commitlint.core.parser.CommitParser;
import com.tiltedwindmills.commitlint.core.rules.CaseType;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.RuleOutcome;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubjectCaseRuleTest {

  private final SubjectCaseRule rule = new SubjectCaseRule();
  private final CommitParser parser = new CommitParser();

  @Test
  void passesLowerCase() {
    final CommitMessage msg = parser.parse("feat: add something");
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, List.of(CaseType.LOWER_CASE));

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void failsWhenNotLowerCase() {
    final CommitMessage msg = parser.parse("feat: Add Something");
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, List.of(CaseType.LOWER_CASE));

    Assertions.assertFalse(outcome.valid());
  }

  @Test
  void neverConditionRejectsMatchingCase() {
    final CommitMessage msg = parser.parse("feat: Add Something");
    final RuleOutcome outcome =
        rule.validate(
            msg,
            Condition.NEVER,
            List.of(CaseType.PASCAL_CASE, CaseType.UPPER_CASE, CaseType.START_CASE));

    Assertions.assertFalse(outcome.valid());
  }

  @Test
  void passesWhenSubjectEmpty() {
    final CommitMessage msg = parser.parse("not conventional");
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, List.of(CaseType.LOWER_CASE));

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void passesWithSentenceCase() {
    final CommitMessage msg = parser.parse("feat: Add something new");
    final RuleOutcome outcome =
        rule.validate(msg, Condition.ALWAYS, List.of(CaseType.SENTENCE_CASE));

    Assertions.assertTrue(outcome.valid());
  }

  @Test
  void passesWhenSubjectPresentButEmpty() {
    final CommitMessage msg =
        new CommitMessage(
            "feat: ",
            "feat: ",
            Optional.of("feat"),
            Optional.empty(),
            Optional.of(""),
            Optional.empty(),
            Optional.empty(),
            false);
    final RuleOutcome outcome = rule.validate(msg, Condition.ALWAYS, List.of(CaseType.LOWER_CASE));

    Assertions.assertTrue(outcome.valid());
  }
}
