package com.tiltedwindmills.commitlint.core.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RuleOverrideTest {

  @Test
  void defaultsAreNull() {
    final var override = new RuleOverride();

    Assertions.assertNull(override.getSeverity());
    Assertions.assertNull(override.getCondition());
    Assertions.assertNull(override.getValue());
  }

  @Test
  void setAndGetSeverity() {
    final var override = new RuleOverride();
    override.setSeverity("WARNING");

    Assertions.assertEquals("WARNING", override.getSeverity());
  }

  @Test
  void setAndGetCondition() {
    final var override = new RuleOverride();
    override.setCondition("NEVER");

    Assertions.assertEquals("NEVER", override.getCondition());
  }

  @Test
  void setAndGetValue() {
    final var override = new RuleOverride();
    override.setValue("72");

    Assertions.assertEquals("72", override.getValue());
  }
}
