package com.tiltedwindmills.commitlint.core.config;

import com.tiltedwindmills.commitlint.core.parser.ParserOptions;
import com.tiltedwindmills.commitlint.core.rules.RuleConfig;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Complete configuration for a commitlint run, including rules, parser options, and ignore
 * predicates.
 *
 * @param rules the map of rule names to their configurations
 * @param parserOptions the options controlling commit message parsing
 * @param ignores custom predicates; if any returns {@code true} for a message, it is skipped
 * @param defaultIgnores whether to automatically ignore merge, revert, and amend commits
 */
public record CommitlintConfig(
    Map<String, RuleConfig<?>> rules,
    ParserOptions parserOptions,
    List<Predicate<String>> ignores,
    boolean defaultIgnores) {}
