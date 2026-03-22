package com.tiltedwindmills.commitlint.core.config;

import org.jspecify.annotations.Nullable;

/**
 * Mutable POJO for per-rule overrides.
 *
 * <p>Each field is optional; {@code null} means "keep the default". Used by Maven and Gradle
 * plugins to accept user-supplied rule configuration that is merged into the default config via
 * {@link ConfigBuilder}.
 */
public final class RuleOverride {

  private @Nullable String severity;
  private @Nullable String condition;
  private @Nullable String value;

  /**
   * Returns the severity override, or {@code null} to keep the default.
   *
   * @return the severity string (e.g. {@code "ERROR"}, {@code "WARNING"}, {@code "DISABLED"})
   */
  public @Nullable String getSeverity() {
    return severity;
  }

  /**
   * Sets the severity override.
   *
   * @param severity the severity string
   */
  public void setSeverity(final @Nullable String severity) {
    this.severity = severity;
  }

  /**
   * Returns the condition override, or {@code null} to keep the default.
   *
   * @return the condition string (e.g. {@code "ALWAYS"}, {@code "NEVER"})
   */
  public @Nullable String getCondition() {
    return condition;
  }

  /**
   * Sets the condition override.
   *
   * @param condition the condition string
   */
  public void setCondition(final @Nullable String condition) {
    this.condition = condition;
  }

  /**
   * Returns the value override, or {@code null} to keep the default.
   *
   * @return the rule-specific value string
   */
  public @Nullable String getValue() {
    return value;
  }

  /**
   * Sets the value override.
   *
   * @param value the rule-specific value string
   */
  public void setValue(final @Nullable String value) {
    this.value = value;
  }
}
