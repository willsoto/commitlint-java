package com.tiltedwindmills.commitlint.gradle;

import javax.inject.Inject;
import org.gradle.api.Named;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

/** Gradle managed type for per-rule configuration. */
public abstract class RuleConfiguration implements Named {

  private final String name;

  @SuppressWarnings("InjectOnConstructorOfAbstractClass")
  @Inject
  public RuleConfiguration(final String name) {
    this.name = name;
  }

  @Override
  @Input
  public String getName() {
    return name;
  }

  @Input
  @Optional
  public abstract Property<String> getSeverity();

  @Input
  @Optional
  public abstract Property<String> getCondition();

  @Input
  @Optional
  public abstract Property<String> getValue();
}
