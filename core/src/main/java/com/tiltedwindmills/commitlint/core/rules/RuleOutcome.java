package com.tiltedwindmills.commitlint.core.rules;

import org.jspecify.annotations.Nullable;

public record RuleOutcome(boolean valid, @Nullable String message) {}
