package com.tiltedwindmills.commitlint.core.lint;

import com.tiltedwindmills.commitlint.core.rules.Severity;
import org.jspecify.annotations.Nullable;

public record LintRuleOutcome(
    boolean valid, Severity severity, String name, @Nullable String message) {}
