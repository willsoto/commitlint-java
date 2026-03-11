package com.tiltedwindmills.commitlint.core.rules.builtin;

import com.tiltedwindmills.commitlint.core.ensure.Ensure;
import com.tiltedwindmills.commitlint.core.parser.CommitMessage;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.Rule;
import com.tiltedwindmills.commitlint.core.rules.RuleOutcome;

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
