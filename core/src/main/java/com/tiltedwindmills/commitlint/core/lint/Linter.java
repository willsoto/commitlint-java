package com.tiltedwindmills.commitlint.core.lint;

import com.tiltedwindmills.commitlint.core.config.CommitlintConfig;
import com.tiltedwindmills.commitlint.core.parser.CommitMessage;
import com.tiltedwindmills.commitlint.core.parser.CommitParser;
import com.tiltedwindmills.commitlint.core.rules.Condition;
import com.tiltedwindmills.commitlint.core.rules.Rule;
import com.tiltedwindmills.commitlint.core.rules.RuleConfig;
import com.tiltedwindmills.commitlint.core.rules.RuleOutcome;
import com.tiltedwindmills.commitlint.core.rules.RuleRegistry;
import com.tiltedwindmills.commitlint.core.rules.RuleRegistry.RegisteredRule;
import com.tiltedwindmills.commitlint.core.rules.Severity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * The main entry point for linting commit messages.
 *
 * <p>Parses a raw commit message string and evaluates it against the configured rules, producing a
 * {@link LintOutcome} that details any errors or warnings.
 */
public final class Linter {

  private final RuleRegistry registry;

  /**
   * Creates a linter backed by the given rule registry.
   *
   * @param registry the registry of available rules
   */
  public Linter(final RuleRegistry registry) {
    this.registry = registry;
  }

  /**
   * Lints a raw commit message against the given configuration.
   *
   * <p>Messages matching the default ignores (merge, revert, amend) or any custom ignore predicates
   * are automatically considered valid.
   *
   * @param raw the raw commit message string
   * @param config the lint configuration specifying which rules to apply
   * @return the lint outcome
   */
  public LintOutcome lint(final String raw, final CommitlintConfig config) {
    if (config.defaultIgnores() && isDefaultIgnored(raw)) {
      return new LintOutcome(raw, true, List.of(), List.of());
    }

    for (final Predicate<String> ignore : config.ignores()) {
      if (ignore.test(raw)) {
        return new LintOutcome(raw, true, List.of(), List.of());
      }
    }

    final CommitParser parser = new CommitParser(config.parserOptions());
    final CommitMessage message = parser.parse(raw);

    final List<LintRuleOutcome> errors = new ArrayList<>();
    final List<LintRuleOutcome> warnings = new ArrayList<>();

    for (final Entry<String, RuleConfig<?>> entry : config.rules().entrySet()) {
      final String name = entry.getKey();
      final RuleConfig<?> ruleConfig = entry.getValue();

      if (ruleConfig.severity() == Severity.DISABLED) {
        continue;
      }

      final Optional<RegisteredRule<?>> registeredOpt = registry.get(name);
      if (registeredOpt.isEmpty()) {
        continue;
      }

      final RuleOutcome outcome = applyRule(registeredOpt.get(), message, ruleConfig);
      if (!outcome.valid()) {
        final LintRuleOutcome lintOutcome =
            new LintRuleOutcome(false, ruleConfig.severity(), name, outcome.message());
        if (ruleConfig.severity() == Severity.ERROR) {
          errors.add(lintOutcome);
        } else {
          warnings.add(lintOutcome);
        }
      }
    }

    final boolean valid = errors.isEmpty();
    return new LintOutcome(raw, valid, errors, warnings);
  }

  @SuppressWarnings("unchecked")
  private <V> RuleOutcome applyRule(
      final RuleRegistry.RegisteredRule<?> registered,
      final CommitMessage message,
      final RuleConfig<?> config) {
    final Rule<V> rule = (com.tiltedwindmills.commitlint.core.rules.Rule<V>) registered.rule();
    final V value = (V) config.value();
    final Condition condition = config.condition();
    return rule.validate(message, condition, value);
  }

  private boolean isDefaultIgnored(final String raw) {
    if (raw.isEmpty()) {
      return false;
    }
    return raw.startsWith("Merge ") || raw.startsWith("Revert ") || raw.startsWith("Amend ");
  }
}
