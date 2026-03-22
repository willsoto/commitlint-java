package com.tiltedwindmills.commitlint.core.parser;

import java.util.Optional;
import java.util.regex.Matcher;

/**
 * Parses raw commit message strings into structured {@link CommitMessage} instances.
 *
 * <p>The parser splits the raw message into header, body, and footer sections, then applies a regex
 * pattern (from {@link ParserOptions}) to extract the type, scope, and subject from the header.
 */
public final class CommitParser {

  private final ParserOptions options;

  /**
   * Creates a parser with the given options.
   *
   * @param options the parser options controlling header pattern matching
   */
  public CommitParser(final ParserOptions options) {
    this.options = options;
  }

  /** Creates a parser with {@linkplain ParserOptions#defaults() default options}. */
  public CommitParser() {
    this(ParserOptions.defaults());
  }

  /**
   * Parses a raw commit message string into a {@link CommitMessage}.
   *
   * @param raw the raw commit message to parse
   * @return the parsed commit message
   */
  public CommitMessage parse(final String raw) {
    if (raw.isEmpty()) {
      return new CommitMessage(
          raw,
          "",
          Optional.empty(),
          Optional.empty(),
          Optional.empty(),
          Optional.empty(),
          Optional.empty(),
          false);
    }

    final String[] lines = raw.split("\n", -1);
    final String header = lines[0];

    String body = null;
    String footer = null;

    if (lines.length > 1) {
      final StringBuilder bodyBuilder = new StringBuilder();
      final StringBuilder footerBuilder = new StringBuilder();
      boolean inFooter = false;
      boolean bodyStarted = false;
      boolean footerStarted = false;

      for (int i = 1; i < lines.length; i++) {
        final String line = lines[i];
        if (!inFooter && isFooterLine(line)) {
          inFooter = true;
        }
        if (inFooter) {
          if (footerStarted) {
            footerBuilder.append("\n");
          }
          footerStarted = true;
          footerBuilder.append(line);
        } else {
          if (bodyStarted) {
            bodyBuilder.append("\n");
          }
          bodyStarted = true;
          bodyBuilder.append(line);
        }
      }

      if (bodyStarted) {
        body = bodyBuilder.toString();
      }
      if (footerStarted) {
        footer = footerBuilder.toString();
      }
    }

    final Matcher matcher = options.headerPattern().matcher(header);
    if (!matcher.matches()) {
      return new CommitMessage(
          raw,
          header,
          Optional.empty(),
          Optional.empty(),
          Optional.empty(),
          Optional.ofNullable(body),
          Optional.ofNullable(footer),
          false);
    }

    final String type = matcher.group(1);
    final String scope = matcher.group(2);
    final String bang = matcher.group(3);
    final String subject = matcher.group(4);

    final boolean isBreaking = "!".equals(bang);

    return new CommitMessage(
        raw,
        header,
        Optional.ofNullable(type),
        Optional.ofNullable(scope),
        Optional.ofNullable(subject),
        Optional.ofNullable(body),
        Optional.ofNullable(footer),
        isBreaking);
  }

  private boolean isFooterLine(final String line) {
    return line.matches("^[\\w-]+:\\s.*$")
        || line.matches("^[\\w-]+\\s#.*$")
        || line.startsWith("BREAKING CHANGE:");
  }
}
