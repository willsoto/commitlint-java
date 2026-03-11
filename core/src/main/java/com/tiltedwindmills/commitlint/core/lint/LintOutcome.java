package com.tiltedwindmills.commitlint.core.lint;

import java.util.List;

public record LintOutcome(
    String input, boolean valid, List<LintRuleOutcome> errors, List<LintRuleOutcome> warnings) {}
