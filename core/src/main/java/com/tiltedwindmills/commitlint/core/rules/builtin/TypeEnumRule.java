package com.tiltedwindmills.commitlint.core.rules.builtin;

import com.tiltedwindmills.commitlint.core.parser.CommitMessage;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.Rule;
import com.tiltedwindmills.commitlint.core.rules.RuleOutcome;
import java.util.List;

public final class TypeEnumRule implements Rule<List<String>> {

  @Override
  public RuleOutcome validate(
      final CommitMessage message, final Condition condition, final List<String> value) {
    if (message.type().isEmpty()) {
      return new RuleOutcome(true, null);
    }
    final boolean valid = condition.test(Ensure.enumMember(message.type().get(), value));
    final String msg =
        valid
            ? null
            : "type must" + (condition == Condition.NEVER ? " not" : "") + " be one of " + value;
    return new RuleOutcome(valid, msg);
  }
}
