package com.tiltedwindmills.commitlint.core.parser;

import java.util.Optional;

public record CommitMessage(
    String raw,
    String header,
    Optional<String> type,
    Optional<String> scope,
    Optional<String> subject,
    Optional<String> body,
    Optional<String> footer,
    boolean isBreaking) {}
