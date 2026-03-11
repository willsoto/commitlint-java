package com.tiltedwindmills.commitlint.core.rules;

public enum Severity {
  DISABLED(0),
  WARNING(1),
  ERROR(2);

  private final int level;

  Severity(final int level) {
    this.level = level;
  }

  public int level() {
    return level;
  }

  public static Severity fromLevel(final int level) {
    for (final Severity s : values()) {
      if (s.level == level) {
        return s;
      }
    }
    throw new IllegalArgumentException("Unknown severity level: " + level);
  }
}
