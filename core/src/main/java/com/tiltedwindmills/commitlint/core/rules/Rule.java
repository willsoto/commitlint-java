package com.tiltedwindmills.commitlint.core.rules;

import com.tiltedwindmills.commitlint.core.parser.CommitMessage;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface Rule<V> {
  RuleOutcome validate(
      final CommitMessage message, final Condition condition, final @Nullable V value);
}
