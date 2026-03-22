package com.tiltedwindmills.commitlint.gradle;

import javax.inject.Inject;
import org.gradle.api.Named;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

/**
 * Gradle-managed configuration for a single lint rule override.
 *
 * <p>Each instance is identified by its {@linkplain #getName() name}, which must match a known rule
 * name (e.g. {@code "header-max-length"}, {@code "type-enum"}).
 */
public abstract class RuleConfiguration implements Named {

  private final String name;

  /**
   * Creates a rule configuration with the given name. Injected by Gradle.
   *
   * @param name the rule name
   */
  @SuppressWarnings("InjectOnConstructorOfAbstractClass")
  @Inject
  public RuleConfiguration(final String name) {
    this.name = name;
  }

  /**
   * Returns the rule name.
   *
   * @return the rule name
   */
  @Override
  @Input
  public String getName() {
    return name;
  }

  /**
   * The severity override (e.g. {@code "ERROR"}, {@code "WARNING"}, {@code "DISABLED"}).
   *
   * @return the severity property
   */
  @Input
  @Optional
  public abstract Property<String> getSeverity();

  /**
   * The condition override (e.g. {@code "ALWAYS"}, {@code "NEVER"}).
   *
   * @return the condition property
   */
  @Input
  @Optional
  public abstract Property<String> getCondition();

  /**
   * The rule-specific value override.
   *
   * @return the value property
   */
  @Input
  @Optional
  public abstract Property<String> getValue();
}
