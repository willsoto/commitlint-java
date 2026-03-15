package com.tiltedwindmills.commitlint.core.config;

import com.tiltedwindmills.commitlint.core.rules.CaseType;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.RuleConfig;
import com.tiltedwindmills.commitlint.core.rules.Severity;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ConfigBuilder {

  private static final Set<String> VOID_RULES = Set.of("type-empty", "body-leading-blank");

  private ConfigBuilder() {}

  public static CommitlintConfig build(
      final Map<String, RuleOverride> overrides, final boolean defaultIgnores) {
    final CommitlintConfig base = DefaultConfig.conventional();
    final Map<String, RuleConfig<?>> rules = new LinkedHashMap<>(base.rules());

    for (final var entry : overrides.entrySet()) {
      final String name = entry.getKey();
      final RuleOverride override = entry.getValue();

      if (!rules.containsKey(name)) {
        throw new IllegalArgumentException("Unknown rule in configuration: " + name);
      }

      final RuleConfig<?> existing = rules.get(name);
      rules.put(name, applyOverride(name, existing, override));
    }

    return new CommitlintConfig(
        Map.copyOf(rules), base.parserOptions(), base.ignores(), defaultIgnores);
  }

  @SuppressWarnings("unchecked")
  private static RuleConfig<?> applyOverride(
      final String name, final RuleConfig<?> existing, final RuleOverride override) {
    final Severity severity =
        override.getSeverity() != null
            ? parseSeverity(override.getSeverity())
            : existing.severity();
    final Condition condition =
        override.getCondition() != null
            ? parseCondition(override.getCondition())
            : existing.condition();
    final Object value =
        override.getValue() != null ? parseValue(name, override.getValue()) : existing.value();

    if (VOID_RULES.contains(name)) {
      return RuleConfig.of(severity, condition);
    }
    return new RuleConfig<>(severity, condition, value);
  }

  private static Severity parseSeverity(final String raw) {
    try {
      return Severity.valueOf(raw);
    } catch (final IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "Invalid severity: " + raw + ". Must be one of: DISABLED, WARNING, ERROR", e);
    }
  }

  private static Condition parseCondition(final String raw) {
    try {
      return Condition.valueOf(raw);
    } catch (final IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "Invalid condition: " + raw + ". Must be one of: ALWAYS, NEVER", e);
    }
  }

  private static Object parseValue(final String ruleName, final String raw) {
    return switch (ruleName) {
      case "header-max-length" -> parseInteger(raw);
      case "type-enum" -> parseStringList(raw);
      case "subject-case" -> parseCaseTypeList(raw);
      default -> raw;
    };
  }

  private static Integer parseInteger(final String raw) {
    try {
      return Integer.valueOf(raw.trim());
    } catch (final NumberFormatException e) {
      throw new IllegalArgumentException("Invalid integer value: " + raw, e);
    }
  }

  private static List<String> parseStringList(final String raw) {
    return Arrays.stream(raw.split(",")).map(String::trim).toList();
  }

  private static List<CaseType> parseCaseTypeList(final String raw) {
    try {
      return Arrays.stream(raw.split(",")).map(String::trim).map(CaseType::valueOf).toList();
    } catch (final IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid case type in value: " + raw, e);
    }
  }
}
