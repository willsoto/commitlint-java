package com.tiltedwindmills.commitlint.gradle;

import javax.inject.Inject;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

/** Extension for configuring the commitlint plugin. */
public abstract class CommitlintExtension {

  private final NamedDomainObjectContainer<RuleConfiguration> rules;

  @SuppressWarnings("InjectOnConstructorOfAbstractClass")
  @Inject
  public CommitlintExtension(final ObjectFactory objects) {
    this.rules = objects.domainObjectContainer(RuleConfiguration.class);
  }

  public abstract RegularFileProperty getCommitMessageFile();

  public abstract Property<Boolean> getFailOnWarning();

  public abstract Property<Boolean> getDefaultIgnores();

  public NamedDomainObjectContainer<RuleConfiguration> getRules() {
    return rules;
  }

  public void rules(final Action<? super NamedDomainObjectContainer<RuleConfiguration>> action) {
    action.execute(rules);
  }
}
