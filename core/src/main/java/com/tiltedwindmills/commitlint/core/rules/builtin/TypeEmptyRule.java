package com.tiltedwindmills.commitlint.core.rules.builtin;

import com.tiltedwindmills.commitlint.core.parser.CommitMessage;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.Rule;
import com.tiltedwindmills.commitlint.core.rules.RuleOutcome;
import org.jspecify.annotations.Nullable;

/**
 * Rule that checks whether the commit type is empty.
 *
 * <p>Typically used with {@link Condition#NEVER} to require that a type is present.
 */
public final class TypeEmptyRule implements Rule<Void> {

  @Override
  public RuleOutcome validate(
      final CommitMessage message, final Condition condition, final @Nullable Void value) {
    final boolean empty = message.type().filter(t -> !t.isEmpty()).isEmpty();
    final boolean valid = condition.test(empty);
    final String msg =
        valid ? null : "type must" + (condition == Condition.NEVER ? " not" : "") + " be empty";
    return new RuleOutcome(valid, msg);
  }
}
