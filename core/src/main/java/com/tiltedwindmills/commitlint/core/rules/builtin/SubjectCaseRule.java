package com.tiltedwindmills.commitlint.core.rules.builtin;

import com.tiltedwindmills.commitlint.core.parser.CommitMessage;
import com.tiltedwindmills.commitlint.core.rules.CaseType;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.Rule;
import com.tiltedwindmills.commitlint.core.rules.RuleOutcome;
import java.util.List;

/**
 * Rule that checks whether the commit subject matches one of the specified {@link CaseType}s.
 *
 * <p>The value parameter is the list of case types to check against.
 */
public final class SubjectCaseRule implements Rule<List<CaseType>> {

  @Override
  public RuleOutcome validate(
      final CommitMessage message, final Condition condition, final List<CaseType> value) {
    if (message.subject().filter(s -> !s.isEmpty()).isEmpty()) {
      return new RuleOutcome(true, null);
    }
    final String subject = message.subject().get();
    final boolean matchesAny = value.stream().anyMatch(ct -> Ensure.matchesCase(subject, ct));
    final boolean valid = condition.test(matchesAny);
    final String msg =
        valid
            ? null
            : "subject must"
                + (condition == Condition.NEVER ? " not" : "")
                + " be in case "
                + value;
    return new RuleOutcome(valid, msg);
  }
}
