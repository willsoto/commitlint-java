package com.tiltedwindmills.commitlint.core.rules.builtin;

import com.tiltedwindmills.commitlint.core.rules.CaseType;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EnsureTest {

  @Test
  void maxLengthPassesWithinLimit() {
    Assertions.assertTrue(Ensure.maxLength("hello", 10));
  }

  @Test
  void maxLengthFailsOverLimit() {
    Assertions.assertFalse(Ensure.maxLength("hello", 3));
  }

  @Test
  void minLengthPassesAboveMin() {
    Assertions.assertTrue(Ensure.minLength("hello", 3));
  }

  @Test
  void minLengthFailsBelowMin() {
    Assertions.assertFalse(Ensure.minLength("hi", 5));
  }

  @Test
  void notEmptyReturnsTrueForNonEmpty() {
    Assertions.assertTrue(Ensure.notEmpty("hello"));
  }

  @Test
  void notEmptyReturnsFalseForEmpty() {
    Assertions.assertFalse(Ensure.notEmpty(""));
  }

  @Test
  void enumMemberPasses() {
    Assertions.assertTrue(Ensure.enumMember("feat", List.of("feat", "fix")));
  }

  @Test
  void enumMemberFails() {
    Assertions.assertFalse(Ensure.enumMember("chore", List.of("feat", "fix")));
  }

  @Test
  void matchesCaseEmpty() {
    Assertions.assertTrue(Ensure.matchesCase("", CaseType.LOWER_CASE));
  }

  @Test
  void matchesCaseCamelCase() {
    Assertions.assertTrue(Ensure.matchesCase("camelCase", CaseType.CAMEL_CASE));
    Assertions.assertFalse(Ensure.matchesCase("PascalCase", CaseType.CAMEL_CASE));
  }

  @Test
  void matchesCaseKebabCase() {
    Assertions.assertTrue(Ensure.matchesCase("kebab-case", CaseType.KEBAB_CASE));
    Assertions.assertFalse(Ensure.matchesCase("notKebab", CaseType.KEBAB_CASE));
  }

  @Test
  void matchesCaseSnakeCase() {
    Assertions.assertTrue(Ensure.matchesCase("snake_case", CaseType.SNAKE_CASE));
    Assertions.assertFalse(Ensure.matchesCase("notSnake", CaseType.SNAKE_CASE));
  }

  @Test
  void matchesCaseSentenceCaseSingleChar() {
    Assertions.assertTrue(Ensure.matchesCase("A", CaseType.SENTENCE_CASE));
  }

  @Test
  void matchesCaseSentenceCaseFails() {
    Assertions.assertFalse(Ensure.matchesCase("not sentence", CaseType.SENTENCE_CASE));
  }

  @Test
  void matchesCaseSentenceCaseMultiWordFails() {
    Assertions.assertFalse(Ensure.matchesCase("Hello WORLD", CaseType.SENTENCE_CASE));
  }

  @Test
  void matchesCaseStartCaseFailsWithLowerWord() {
    Assertions.assertFalse(Ensure.matchesCase("Hello world", CaseType.START_CASE));
  }
}
