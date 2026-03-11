package com.tiltedwindmills.commitlint.core.parser;

import java.util.List;
import java.util.regex.Pattern;

public record ParserOptions(Pattern headerPattern, List<String> headerCorrespondence) {

  public static ParserOptions defaults() {
    return new ParserOptions(
        Pattern.compile("^(\\w+)(?:\\(([^)]*)\\))?(!)?:\\s*(.*)$"),
        List.of("type", "scope", "bang", "subject"));
  }
}
