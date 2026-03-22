package com.tiltedwindmills.commitlint.core.rules;

/**
 * Determines whether a rule's assertion should hold as-is or be negated.
 *
 * <p>For example, a "type-empty" rule with {@code NEVER} means the type must <em>never</em> be
 * empty.
 */
public enum Condition {
  /** The assertion must hold (pass-through). */
  ALWAYS,

  /** The assertion is negated. */
  NEVER;

  /**
   * Applies this condition to a boolean value.
   *
   * @param value the raw assertion result
   * @return {@code value} if {@link #ALWAYS}, {@code !value} if {@link #NEVER}
   */
  public boolean test(final boolean value) {
    return this == ALWAYS ? value : !value;
  }
}
