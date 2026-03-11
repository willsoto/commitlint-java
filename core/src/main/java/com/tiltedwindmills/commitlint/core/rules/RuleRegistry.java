package com.tiltedwindmills.commitlint.core.rules;

import com.tiltedwindmills.commitlint.core.rules.builtin.BodyLeadingBlankRule;
import com.tiltedwindmills.commitlint.core.rules.builtin.HeaderMaxLengthRule;
import com.tiltedwindmills.commitlint.core.rules.builtin.SubjectCaseRule;
import com.tiltedwindmills.commitlint.core.rules.builtin.TypeEmptyRule;
import com.tiltedwindmills.commitlint.core.rules.builtin.TypeEnumRule;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class RuleRegistry {

  public record RegisteredRule<V>(Rule<V> rule, Class<V> valueType) {}

  private final Map<String, RegisteredRule<?>> rules = new HashMap<>();

  public <V> void register(final String name, final Rule<V> rule, final Class<V> valueType) {
    rules.put(name, new RegisteredRule<>(rule, valueType));
  }

  public Optional<RegisteredRule<?>> get(final String name) {
    return Optional.ofNullable(rules.get(name));
  }

  public Map<String, RegisteredRule<?>> all() {
    return Collections.unmodifiableMap(rules);
  }

  @SuppressWarnings("unchecked")
  public static RuleRegistry withBuiltins() {
    final RuleRegistry registry = new RuleRegistry();
    registry.register("header-max-length", new HeaderMaxLengthRule(), Integer.class);
    registry.register("type-empty", new TypeEmptyRule(), Void.class);
    registry.register("type-enum", new TypeEnumRule(), (Class<List<String>>) (Class<?>) List.class);
    registry.register(
        "subject-case", new SubjectCaseRule(), (Class<List<CaseType>>) (Class<?>) List.class);
    registry.register("body-leading-blank", new BodyLeadingBlankRule(), Void.class);
    return registry;
  }
}
