package com.tiltedwindmills.commitlint.core.config;

import com.tiltedwindmills.commitlint.core.rules.CaseType;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.Severity;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConfigBuilderTest {

  @Test
  void noOverridesReturnsConventionalDefaults() {
    final var config = ConfigBuilder.build(Map.of(), true);

    Assertions.assertEquals(5, config.rules().size());
    Assertions.assertTrue(config.defaultIgnores());
  }

  @Test
  void overrideSeverityOnly() {
    final var override = new RuleOverride();
    override.setSeverity("WARNING");
    final var config = ConfigBuilder.build(Map.of("header-max-length", override), true);

    final var rule = config.rules().get("header-max-length");
    Assertions.assertEquals(Severity.WARNING, rule.severity());
    Assertions.assertEquals(Condition.ALWAYS, rule.condition());
    Assertions.assertEquals(100, rule.value());
  }

  @Test
  void overrideConditionOnly() {
    final var override = new RuleOverride();
    override.setCondition("ALWAYS");
    final var config = ConfigBuilder.build(Map.of("type-empty", override), true);

    final var rule = config.rules().get("type-empty");
    Assertions.assertEquals(Severity.ERROR, rule.severity());
    Assertions.assertEquals(Condition.ALWAYS, rule.condition());
  }

  @Test
  void overrideHeaderMaxLengthValue() {
    final var override = new RuleOverride();
    override.setValue("72");
    final var config = ConfigBuilder.build(Map.of("header-max-length", override), true);

    final var rule = config.rules().get("header-max-length");
    Assertions.assertEquals(72, rule.value());
  }

  @Test
  void overrideTypeEnumValue() {
    final var override = new RuleOverride();
    override.setValue("feat,fix,chore");
    final var config = ConfigBuilder.build(Map.of("type-enum", override), true);

    Assertions.assertEquals(
        List.of("feat", "fix", "chore"), config.rules().get("type-enum").value());
  }

  @Test
  void overrideSubjectCaseValue() {
    final var override = new RuleOverride();
    override.setValue("UPPER_CASE,KEBAB_CASE");
    final var config = ConfigBuilder.build(Map.of("subject-case", override), true);

    Assertions.assertEquals(
        List.of(CaseType.UPPER_CASE, CaseType.KEBAB_CASE),
        config.rules().get("subject-case").value());
  }

  @Test
  void disableRule() {
    final var override = new RuleOverride();
    override.setSeverity("DISABLED");
    final var config = ConfigBuilder.build(Map.of("type-empty", override), true);

    Assertions.assertEquals(Severity.DISABLED, config.rules().get("type-empty").severity());
  }

  @Test
  void unknownRuleThrows() {
    final var override = new RuleOverride();
    override.setSeverity("ERROR");

    final var exception =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> ConfigBuilder.build(Map.of("unknown-rule", override), true));

    Assertions.assertTrue(exception.getMessage().contains("unknown-rule"));
  }

  @Test
  void invalidSeverityThrows() {
    final var override = new RuleOverride();
    override.setSeverity("CRITICAL");

    final var exception =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> ConfigBuilder.build(Map.of("type-empty", override), true));

    Assertions.assertTrue(exception.getMessage().contains("CRITICAL"));
  }

  @Test
  void invalidConditionThrows() {
    final var override = new RuleOverride();
    override.setCondition("SOMETIMES");

    final var exception =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> ConfigBuilder.build(Map.of("type-empty", override), true));

    Assertions.assertTrue(exception.getMessage().contains("SOMETIMES"));
  }

  @Test
  void defaultIgnoresFalseIsPassedThrough() {
    final var config = ConfigBuilder.build(Map.of(), false);

    Assertions.assertFalse(config.defaultIgnores());
  }

  @Test
  void valueIgnoredForVoidRules() {
    final var override = new RuleOverride();
    override.setValue("anything");
    final var config = ConfigBuilder.build(Map.of("type-empty", override), true);

    Assertions.assertNull(config.rules().get("type-empty").value());
  }

  @Test
  void invalidIntegerValueThrows() {
    final var override = new RuleOverride();
    override.setValue("not-a-number");

    final var exception =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> ConfigBuilder.build(Map.of("header-max-length", override), true));

    Assertions.assertTrue(exception.getMessage().contains("not-a-number"));
  }

  @Test
  void invalidCaseTypeValueThrows() {
    final var override = new RuleOverride();
    override.setValue("INVALID_CASE");

    final var exception =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> ConfigBuilder.build(Map.of("subject-case", override), true));

    Assertions.assertTrue(exception.getMessage().contains("INVALID_CASE"));
  }

  @Test
  void multipleOverridesApplied() {
    final var headerOverride = new RuleOverride();
    headerOverride.setValue("50");
    final var bodyOverride = new RuleOverride();
    bodyOverride.setSeverity("ERROR");
    final var config =
        ConfigBuilder.build(
            Map.of("header-max-length", headerOverride, "body-leading-blank", bodyOverride), true);

    Assertions.assertEquals(50, config.rules().get("header-max-length").value());
    Assertions.assertEquals(Severity.ERROR, config.rules().get("body-leading-blank").severity());
  }
}
