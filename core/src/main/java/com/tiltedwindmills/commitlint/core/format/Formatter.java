package com.tiltedwindmills.commitlint.core.format;

import com.tiltedwindmills.commitlint.core.lint.LintOutcome;
import com.tiltedwindmills.commitlint.core.lint.LintRuleOutcome;

/**
 * Formats a {@link LintOutcome} into a human-readable string suitable for console output.
 *
 * <p>The output includes a pass/fail marker, the input message, individual error and warning
 * details, and a summary count.
 */
@SuppressWarnings("UnnecessaryUnicodeEscape")
public final class Formatter {

  /**
   * Formats the given lint outcome as a human-readable report.
   *
   * @param outcome the lint outcome to format
   * @return the formatted report string
   */
  public String format(final LintOutcome outcome) {
    final StringBuilder sb = new StringBuilder();

    final String marker = outcome.valid() ? "\u2714" : "\u2716";
    sb.append(marker).append(" input: ").append(outcome.input()).append("\n");

    for (final LintRuleOutcome error : outcome.errors()) {
      sb.append("  \u2716 ")
          .append(error.message())
          .append(" [")
          .append(error.name())
          .append("]\n");
    }

    for (final LintRuleOutcome warning : outcome.warnings()) {
      sb.append("  \u26A0 ")
          .append(warning.message())
          .append(" [")
          .append(warning.name())
          .append("]\n");
    }

    final int errorCount = outcome.errors().size();
    final int warningCount = outcome.warnings().size();
    sb.append("\n")
        .append(errorCount)
        .append(" error(s), ")
        .append(warningCount)
        .append(" warning(s)");

    return sb.toString();
  }
}
