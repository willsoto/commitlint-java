package com.tiltedwindmills.commitlint.core.config;

import com.tiltedwindmills.commitlint.core.parser.ParserOptions;
import com.tiltedwindmills.commitlint.core.rules.RuleConfig;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public record CommitlintConfig(
    Map<String, RuleConfig<?>> rules,
    ParserOptions parserOptions,
    List<Predicate<String>> ignores,
    boolean defaultIgnores) {}
