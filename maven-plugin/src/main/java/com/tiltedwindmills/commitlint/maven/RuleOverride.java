package com.tiltedwindmills.commitlint.maven;

import org.jspecify.annotations.Nullable;

/** Mutable POJO for Maven XML binding of per-rule overrides. */
public final class RuleOverride {

  private @Nullable String severity;
  private @Nullable String condition;
  private @Nullable String value;

  public @Nullable String getSeverity() {
    return severity;
  }

  public void setSeverity(final String severity) {
    this.severity = severity;
  }

  public @Nullable String getCondition() {
    return condition;
  }

  public void setCondition(final String condition) {
    this.condition = condition;
  }

  public @Nullable String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }
}
