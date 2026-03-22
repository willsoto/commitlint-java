package com.tiltedwindmills.commitlint.core.rules.builtin;

import com.google.common.base.Splitter;
import com.tiltedwindmills.commitlint.core.rules.CaseType;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Pattern;

/** Utility methods for common validation checks used by rule implementations. */
final class Ensure {

  private Ensure() {}

  /**
   * Returns {@code true} if the value's length is at most {@code max}.
   *
   * @param value the string to check
   * @param max the maximum allowed length
   * @return whether the length constraint is satisfied
   */
  static boolean maxLength(final String value, final int max) {
    return value.length() <= max;
  }

  /**
   * Returns {@code true} if the value's length is at least {@code min}.
   *
   * @param value the string to check
   * @param min the minimum required length
   * @return whether the length constraint is satisfied
   */
  static boolean minLength(final String value, final int min) {
    return value.length() >= min;
  }

  /**
   * Returns {@code true} if the value is not empty.
   *
   * @param value the string to check
   * @return whether the string is non-empty
   */
  static boolean notEmpty(final String value) {
    return !value.isEmpty();
  }

  /**
   * Returns {@code true} if the value is contained in the allowed collection.
   *
   * @param value the string to check
   * @param allowed the collection of allowed values
   * @return whether the value is a member of the allowed set
   */
  static boolean enumMember(final String value, final Collection<String> allowed) {
    return allowed.contains(value);
  }

  /**
   * Returns {@code true} if the value matches the specified case type.
   *
   * <p>An empty string is considered to match any case type.
   *
   * @param value the string to check
   * @param caseType the case type to match against
   * @return whether the value matches the case type
   */
  static boolean matchesCase(final String value, final CaseType caseType) {
    if (value.isEmpty()) {
      return true;
    }
    return switch (caseType) {
      case LOWER_CASE -> value.equals(value.toLowerCase(Locale.ROOT));
      case UPPER_CASE -> value.equals(value.toUpperCase(Locale.ROOT));
      case CAMEL_CASE -> value.matches("^[a-z][a-zA-Z0-9]*$");
      case KEBAB_CASE -> value.matches("^[a-z][a-z0-9]*(-[a-z0-9]+)*$");
      case SNAKE_CASE -> value.matches("^[a-z][a-z0-9]*(_[a-z0-9]+)*$");
      case PASCAL_CASE -> value.matches("^[A-Z][a-zA-Z0-9]*$");
      case SENTENCE_CASE ->
          Character.isUpperCase(value.charAt(0))
              && (value.length() == 1
                  || value.substring(1).equals(value.substring(1).toLowerCase(Locale.ROOT)));
      case START_CASE -> {
        final Iterable<String> words = Splitter.on(Pattern.compile("\\s+")).split(value);
        for (final String word : words) {
          if (word.isEmpty() || !Character.isUpperCase(word.charAt(0))) {
            yield false;
          }
        }
        yield true;
      }
    };
  }
}
