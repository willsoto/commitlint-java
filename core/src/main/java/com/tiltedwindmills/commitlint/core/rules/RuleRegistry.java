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

/**
 * A registry that maps rule names to their {@link Rule} implementations.
 *
 * <p>Use {@link #withBuiltins()} to obtain a registry pre-populated with all built-in rules, or
 * create an empty registry and register custom rules via {@link #register}.
 */
public final class RuleRegistry {

  /**
   * A rule paired with its value type, as stored in the registry.
   *
   * @param <V> the rule's value type
   * @param rule the rule implementation
   * @param valueType the class of the rule's value parameter
   */
  public record RegisteredRule<V>(Rule<V> rule, Class<V> valueType) {}

  private final Map<String, RegisteredRule<?>> rules = new HashMap<>();

  /**
   * Registers a rule under the given name.
   *
   * @param <V> the rule's value type
   * @param name the rule name (e.g. {@code "header-max-length"})
   * @param rule the rule implementation
   * @param valueType the class of the rule's value parameter
   */
  public <V> void register(final String name, final Rule<V> rule, final Class<V> valueType) {
    rules.put(name, new RegisteredRule<>(rule, valueType));
  }

  /**
   * Looks up a rule by name.
   *
   * @param name the rule name
   * @return the registered rule, or empty if no rule is registered under that name
   */
  public Optional<RegisteredRule<?>> get(final String name) {
    return Optional.ofNullable(rules.get(name));
  }

  /**
   * Returns an unmodifiable view of all registered rules.
   *
   * @return a map of rule names to registered rules
   */
  public Map<String, RegisteredRule<?>> all() {
    return Collections.unmodifiableMap(rules);
  }

  /**
   * Creates a new registry pre-populated with all built-in rules.
   *
   * @return a registry containing the built-in rules
   */
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
