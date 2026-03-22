package com.tiltedwindmills.commitlint.core.rules;

import com.tiltedwindmills.commitlint.core.parser.CommitMessage;
import org.jspecify.annotations.Nullable;

/**
 * A lint rule that validates a parsed commit message.
 *
 * @param <V> the type of the rule's configuration value, or {@link Void} for rules that take no
 *     value
 */
@FunctionalInterface
public interface Rule<V> {

  /**
   * Validates the given commit message against this rule.
   *
   * @param message the parsed commit message to validate
   * @param condition whether the rule's assertion should hold ({@link Condition#ALWAYS}) or be
   *     negated ({@link Condition#NEVER})
   * @param value the rule-specific configuration value, or {@code null} for {@link Void}-typed
   *     rules
   * @return the outcome indicating whether the message passed validation
   */
  RuleOutcome validate(
      final CommitMessage message, final Condition condition, final @Nullable V value);
}
