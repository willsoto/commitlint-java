package com.tiltedwindmills.commitlint.core.rules;

/** Supported casing styles for commit message fields. */
public enum CaseType {
  /** All lowercase, e.g. {@code some text}. */
  LOWER_CASE,

  /** All uppercase, e.g. {@code SOME TEXT}. */
  UPPER_CASE,

  /** Camel case starting with a lowercase letter, e.g. {@code someText}. */
  CAMEL_CASE,

  /** Lowercase words separated by hyphens, e.g. {@code some-text}. */
  KEBAB_CASE,

  /** Lowercase words separated by underscores, e.g. {@code some_text}. */
  SNAKE_CASE,

  /** Camel case starting with an uppercase letter, e.g. {@code SomeText}. */
  PASCAL_CASE,

  /** First letter uppercase, rest lowercase, e.g. {@code Some text}. */
  SENTENCE_CASE,

  /** First letter of every word uppercase, e.g. {@code Some Text}. */
  START_CASE
}
