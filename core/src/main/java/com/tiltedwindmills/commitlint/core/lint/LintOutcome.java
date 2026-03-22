package com.tiltedwindmills.commitlint.core.lint;

import java.util.List;

/**
 * The aggregate result of linting a commit message against all configured rules.
 *
 * @param input the original commit message that was linted
 * @param valid whether the message passed all error-level rules
 * @param errors the list of error-level rule violations
 * @param warnings the list of warning-level rule violations
 */
public record LintOutcome(
    String input, boolean valid, List<LintRuleOutcome> errors, List<LintRuleOutcome> warnings) {}
