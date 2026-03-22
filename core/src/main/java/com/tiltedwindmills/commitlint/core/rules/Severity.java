package com.tiltedwindmills.commitlint.core.rules;

/** The severity level of a lint rule, determining how violations are reported. */
public enum Severity {
  /** The rule is disabled and will not be evaluated. */
  DISABLED(0),

  /** Violations are reported as warnings but do not cause lint to fail. */
  WARNING(1),

  /** Violations are reported as errors and cause lint to fail. */
  ERROR(2);

  private final int level;

  Severity(final int level) {
    this.level = level;
  }

  /**
   * Returns the numeric level for this severity.
   *
   * @return the level ({@code 0} for disabled, {@code 1} for warning, {@code 2} for error)
   */
  public int level() {
    return level;
  }

  /**
   * Returns the severity corresponding to the given numeric level.
   *
   * @param level the numeric level
   * @return the matching severity
   * @throws IllegalArgumentException if no severity matches the given level
   */
  public static Severity fromLevel(final int level) {
    for (final Severity s : values()) {
      if (s.level == level) {
        return s;
      }
    }
    throw new IllegalArgumentException("Unknown severity level: " + level);
  }
}
