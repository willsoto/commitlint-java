package com.tiltedwindmills.commitlint.core.ensure;

import com.google.common.base.Splitter;
import com.tiltedwindmills.commitlint.core.rules.CaseType;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Pattern;

public final class Ensure {

  private Ensure() {}

  public static boolean maxLength(final String value, final int max) {
    return value.length() <= max;
  }

  public static boolean minLength(final String value, final int min) {
    return value.length() >= min;
  }

  public static boolean notEmpty(final String value) {
    return !value.isEmpty();
  }

  public static boolean enumMember(final String value, final Collection<String> allowed) {
    return allowed.contains(value);
  }

  public static boolean matchesCase(final String value, final CaseType caseType) {
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
