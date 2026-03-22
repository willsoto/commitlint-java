package com.tiltedwindmills.commitlint.core.parser;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Options controlling how commit message headers are parsed.
 *
 * @param headerPattern the regex pattern used to extract components from the header line
 * @param headerCorrespondence the names of the capture groups in the header pattern
 */
public record ParserOptions(Pattern headerPattern, List<String> headerCorrespondence) {

  /**
   * Returns the default parser options matching the Conventional Commits format: {@code
   * type(scope)!: subject}.
   *
   * @return the default parser options
   */
  public static ParserOptions defaults() {
    return new ParserOptions(
        Pattern.compile("^(\\w+)(?:\\(([^)]*)\\))?(!)?:\\s*(.*)$"),
        List.of("type", "scope", "bang", "subject"));
  }
}
