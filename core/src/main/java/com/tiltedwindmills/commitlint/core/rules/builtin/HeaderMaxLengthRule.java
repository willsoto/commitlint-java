package com.tiltedwindmills.commitlint.core.rules.builtin;

import com.tiltedwindmills.commitlint.core.parser.CommitMessage;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.Rule;
import com.tiltedwindmills.commitlint.core.rules.RuleOutcome;

/**
 * Rule that enforces a maximum length on the commit header.
 *
 * <p>The value parameter specifies the maximum allowed number of characters.
 */
public final class HeaderMaxLengthRule implements Rule<Integer> {

  @Override
  public RuleOutcome validate(
      final CommitMessage message, final Condition condition, final Integer value) {
    final boolean valid = condition.test(Ensure.maxLength(message.header(), value));
    final String msg =
        valid
            ? null
            : "header must"
                + (condition == Condition.NEVER ? " not" : "")
                + " be at most "
                + value
                + " characters";
    return new RuleOutcome(valid, msg);
  }
}
