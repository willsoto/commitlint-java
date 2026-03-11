package com.tiltedwindmills.commitlint.core.rules;

public enum Condition {
  ALWAYS,
  NEVER;

  public boolean test(final boolean value) {
    return this == ALWAYS ? value : !value;
  }
}
