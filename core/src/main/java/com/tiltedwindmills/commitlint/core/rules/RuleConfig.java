package com.tiltedwindmills.commitlint.core.rules;

import org.jspecify.annotations.Nullable;

public record RuleConfig<V>(Severity severity, Condition condition, @Nullable V value) {

  public static <V> RuleConfig<V> of(
      final Severity severity, final Condition condition, final V value) {
    return new RuleConfig<>(severity, condition, value);
  }

  public static RuleConfig<Void> of(final Severity severity, final Condition condition) {
    return new RuleConfig<>(severity, condition, null);
  }
}
