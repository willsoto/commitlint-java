package com.tiltedwindmills.commitlint.core.format;

import com.tiltedwindmills.commitlint.core.lint.LintOutcome;
import com.tiltedwindmills.commitlint.core.lint.LintRuleOutcome;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public final class Formatter {

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
