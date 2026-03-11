package com.tiltedwindmills.commitlint.core.rules;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SeverityTest {

  @Test
  void fromLevelReturnsDisabled() {
    Assertions.assertEquals(Severity.DISABLED, Severity.fromLevel(0));
  }

  @Test
  void fromLevelReturnsWarning() {
    Assertions.assertEquals(Severity.WARNING, Severity.fromLevel(1));
  }

  @Test
  void fromLevelReturnsError() {
    Assertions.assertEquals(Severity.ERROR, Severity.fromLevel(2));
  }

  @Test
  void fromLevelThrowsForUnknown() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> Severity.fromLevel(99));
  }
}
