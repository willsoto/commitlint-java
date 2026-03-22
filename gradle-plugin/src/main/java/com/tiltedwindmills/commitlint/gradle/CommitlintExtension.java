package com.tiltedwindmills.commitlint.gradle;

import javax.inject.Inject;
import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

/**
 * DSL extension for configuring the commitlint Gradle plugin.
 *
 * <p>Example usage in a build script:
 *
 * <pre>{@code
 * commitlint {
 *   commitMessageFile = layout.projectDirectory.file(".git/COMMIT_EDITMSG")
 *   failOnWarning = true
 *   defaultIgnores = true
 *   rules {
 *     register("header-max-length") {
 *       value = "72"
 *     }
 *   }
 * }
 * }</pre>
 */
public abstract class CommitlintExtension {

  private final NamedDomainObjectContainer<RuleConfiguration> rules;

  /**
   * Creates the extension. Injected by Gradle.
   *
   * @param objects the Gradle object factory
   */
  @SuppressWarnings("InjectOnConstructorOfAbstractClass")
  @Inject
  public CommitlintExtension(final ObjectFactory objects) {
    this.rules = objects.domainObjectContainer(RuleConfiguration.class);
  }

  /**
   * The commit message file to lint. Defaults to {@code .git/COMMIT_EDITMSG}.
   *
   * @return the commit message file property
   */
  public abstract RegularFileProperty getCommitMessageFile();

  /**
   * Whether to fail the build on warnings. Defaults to {@code true}.
   *
   * @return the fail-on-warning property
   */
  public abstract Property<Boolean> getFailOnWarning();

  /**
   * Whether to automatically ignore merge, revert, and amend commits. Defaults to {@code true}.
   *
   * @return the default-ignores property
   */
  public abstract Property<Boolean> getDefaultIgnores();

  /**
   * Returns the container of per-rule configuration overrides.
   *
   * @return the rule configuration container
   */
  public NamedDomainObjectContainer<RuleConfiguration> getRules() {
    return rules;
  }

  /**
   * Configures the per-rule overrides using an action.
   *
   * @param action the configuration action
   */
  public void rules(final Action<? super NamedDomainObjectContainer<RuleConfiguration>> action) {
    action.execute(rules);
  }
}
