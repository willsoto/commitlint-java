package com.tiltedwindmills.commitlint.core.parser;

import java.util.Optional;

/**
 * A parsed representation of a commit message following the Conventional Commits format.
 *
 * @param raw the original, unparsed commit message
 * @param header the first line of the commit message
 * @param type the commit type (e.g. {@code feat}, {@code fix}), if present
 * @param scope the commit scope (e.g. {@code parser}, {@code cli}), if present
 * @param subject the commit subject (the description after the colon), if present
 * @param body the commit body (everything between the header and footer), if present
 * @param footer the commit footer (e.g. {@code BREAKING CHANGE:} trailers), if present
 * @param isBreaking whether the commit includes a breaking change indicator ({@code !})
 */
public record CommitMessage(
    String raw,
    String header,
    Optional<String> type,
    Optional<String> scope,
    Optional<String> subject,
    Optional<String> body,
    Optional<String> footer,
    boolean isBreaking) {}
