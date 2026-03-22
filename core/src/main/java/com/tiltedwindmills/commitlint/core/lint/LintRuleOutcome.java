package com.tiltedwindmills.commitlint.core.lint;

import com.tiltedwindmills.commitlint.core.rules.Severity;
import org.jspecify.annotations.Nullable;

/**
 * The result of evaluating a single rule during linting, including the rule's identity and
 * severity.
 *
 * @param valid whether the commit message passed this rule
 * @param severity the severity level of this rule
 * @param name the name of the rule that was evaluated
 * @param message a human-readable failure description, or {@code null} if valid
 */
public record LintRuleOutcome(
    boolean valid, Severity severity, String name, @Nullable String message) {}
