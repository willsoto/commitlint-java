package com.tiltedwindmills.commitlint.core.rules;

import org.jspecify.annotations.Nullable;

/**
 * The result of applying a single {@link Rule} to a commit message.
 *
 * @param valid whether the commit message passed the rule
 * @param message a human-readable failure description, or {@code null} if valid
 */
public record RuleOutcome(boolean valid, @Nullable String message) {}
