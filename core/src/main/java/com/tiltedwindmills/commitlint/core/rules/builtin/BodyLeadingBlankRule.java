package com.tiltedwindmills.commitlint.core.rules.builtin;

import com.tiltedwindmills.commitlint.core.parser.CommitMessage;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.Rule;
import com.tiltedwindmills.commitlint.core.rules.RuleOutcome;
import org.jspecify.annotations.Nullable;

/**
 * Rule that checks whether the commit body is preceded by a blank line.
 *
 * <p>Per the Conventional Commits spec, the body should be separated from the header by an empty
 * line.
 */
public final class BodyLeadingBlankRule implements Rule<Void> {

  @Override
  public RuleOutcome validate(
      final CommitMessage message, final Condition condition, final @Nullable Void value) {
    if (message.body().isEmpty()) {
      return new RuleOutcome(true, null);
    }
    final String body = message.body().get();
    final boolean hasLeadingBlank = body.startsWith("\n") || body.isEmpty();
    final boolean valid = condition.test(hasLeadingBlank);
    final String msg =
        valid
            ? null
            : "body must"
                + (condition == Condition.NEVER ? " not" : "")
                + " have a leading blank line";
    return new RuleOutcome(valid, msg);
  }
}
