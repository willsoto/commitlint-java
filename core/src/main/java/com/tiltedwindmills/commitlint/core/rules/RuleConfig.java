package com.tiltedwindmills.commitlint.core.rules;

import org.jspecify.annotations.Nullable;

/**
 * Configuration for a single lint rule, specifying how and when it should be applied.
 *
 * @param <V> the type of the rule's value parameter
 * @param severity the severity level ({@link Severity#DISABLED}, {@link Severity#WARNING}, or
 *     {@link Severity#ERROR})
 * @param condition whether the rule's assertion should hold or be negated
 * @param value the rule-specific configuration value, or {@code null} for value-less rules
 */
public record RuleConfig<V>(Severity severity, Condition condition, @Nullable V value) {

  /**
   * Creates a rule configuration with a value.
   *
   * @param <V> the value type
   * @param severity the severity level
   * @param condition the condition
   * @param value the rule-specific value
   * @return the rule configuration
   */
  public static <V> RuleConfig<V> of(
      final Severity severity, final Condition condition, final V value) {
    return new RuleConfig<>(severity, condition, value);
  }

  /**
   * Creates a rule configuration for a value-less ({@link Void}) rule.
   *
   * @param severity the severity level
   * @param condition the condition
   * @return the rule configuration
   */
  public static RuleConfig<Void> of(final Severity severity, final Condition condition) {
    return new RuleConfig<>(severity, condition, null);
  }
}
