package com.tiltedwindmills.commitlint.core.config;

import com.tiltedwindmills.commitlint.core.parser.ParserOptions;
import com.tiltedwindmills.commitlint.core.rules.CaseType;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.RuleConfig;
import com.tiltedwindmills.commitlint.core.rules.Severity;
import java.util.List;
import java.util.Map;

/** Provides the default Conventional Commits configuration. */
public final class DefaultConfig {

  private DefaultConfig() {}

  /**
   * Returns the default configuration matching the {@code @commitlint/config-conventional} preset.
   *
   * <p>Includes rules for header max length (100), type-empty (never), type-enum (standard types),
   * subject-case (no pascal/upper/start), and body-leading-blank (warning).
   *
   * @return the conventional commits configuration
   */
  public static CommitlintConfig conventional() {
    return new CommitlintConfig(
        Map.of(
            "header-max-length", RuleConfig.of(Severity.ERROR, Condition.ALWAYS, 100),
            "type-empty", RuleConfig.of(Severity.ERROR, Condition.NEVER),
            "type-enum",
                RuleConfig.of(
                    Severity.ERROR,
                    Condition.ALWAYS,
                    List.of(
                        "feat",
                        "fix",
                        "docs",
                        "style",
                        "refactor",
                        "test",
                        "chore",
                        "perf",
                        "ci",
                        "build",
                        "revert")),
            "subject-case",
                RuleConfig.of(
                    Severity.ERROR,
                    Condition.NEVER,
                    List.of(CaseType.PASCAL_CASE, CaseType.UPPER_CASE, CaseType.START_CASE)),
            "body-leading-blank", RuleConfig.of(Severity.WARNING, Condition.ALWAYS)),
        ParserOptions.defaults(),
        List.of(),
        true);
  }
}
