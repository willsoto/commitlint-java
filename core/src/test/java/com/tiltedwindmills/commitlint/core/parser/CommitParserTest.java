package com.tiltedwindmills.commitlint.core.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommitParserTest {

  private final CommitParser parser = new CommitParser();

  @Test
  void parsesSimpleCommit() {
    final CommitMessage msg = parser.parse("feat: add new feature");

    Assertions.assertEquals("feat", msg.type().orElseThrow());
    Assertions.assertTrue(msg.scope().isEmpty());
    Assertions.assertEquals("add new feature", msg.subject().orElseThrow());
    Assertions.assertEquals("feat: add new feature", msg.header());
    Assertions.assertTrue(msg.body().isEmpty());
    Assertions.assertTrue(msg.footer().isEmpty());
    Assertions.assertFalse(msg.isBreaking());
  }

  @Test
  void parsesCommitWithScope() {
    final CommitMessage msg = parser.parse("fix(parser): handle null input");

    Assertions.assertEquals("fix", msg.type().orElseThrow());
    Assertions.assertEquals("parser", msg.scope().orElseThrow());
    Assertions.assertEquals("handle null input", msg.subject().orElseThrow());
  }

  @Test
  void parsesBreakingChangeWithBang() {
    final CommitMessage msg = parser.parse("feat!: remove deprecated API");

    Assertions.assertEquals("feat", msg.type().orElseThrow());
    Assertions.assertTrue(msg.isBreaking());
    Assertions.assertEquals("remove deprecated API", msg.subject().orElseThrow());
  }

  @Test
  void parsesBreakingChangeWithScopeAndBang() {
    final CommitMessage msg = parser.parse("feat(api)!: remove deprecated endpoint");

    Assertions.assertEquals("feat", msg.type().orElseThrow());
    Assertions.assertEquals("api", msg.scope().orElseThrow());
    Assertions.assertTrue(msg.isBreaking());
  }

  @Test
  void parsesCommitWithBody() {
    final String raw = "feat: add feature\n\nThis is the body.";
    final CommitMessage msg = parser.parse(raw);

    Assertions.assertEquals("feat", msg.type().orElseThrow());
    Assertions.assertEquals("add feature", msg.subject().orElseThrow());
    Assertions.assertEquals("\nThis is the body.", msg.body().orElseThrow());
  }

  @Test
  void parsesCommitWithFooter() {
    final String raw = "fix: bug\n\nBody text.\n\nReviewed-by: Z\nRefs: #123";
    final CommitMessage msg = parser.parse(raw);

    Assertions.assertEquals("fix", msg.type().orElseThrow());
    Assertions.assertTrue(msg.body().isPresent());
    Assertions.assertTrue(msg.footer().isPresent());
    Assertions.assertTrue(msg.footer().get().contains("Reviewed-by: Z"));
  }

  @Test
  void handlesEmptyInput() {
    final CommitMessage msg = parser.parse("");

    Assertions.assertTrue(msg.type().isEmpty());
    Assertions.assertEquals("", msg.header());
  }

  @Test
  void handlesNonConventionalCommit() {
    final CommitMessage msg = parser.parse("just a random message");

    Assertions.assertTrue(msg.type().isEmpty());
    Assertions.assertTrue(msg.scope().isEmpty());
    Assertions.assertTrue(msg.subject().isEmpty());
    Assertions.assertEquals("just a random message", msg.header());
  }

  @Test
  void preservesRawMessage() {
    final String raw = "feat(scope): subject\n\nbody";
    final CommitMessage msg = parser.parse(raw);

    Assertions.assertEquals(raw, msg.raw());
  }

  @Test
  void parsesFooterWithHashSyntax() {
    final String raw = "fix: bug\n\nBody.\n\nCloses #42";
    final CommitMessage msg = parser.parse(raw);

    Assertions.assertTrue(msg.footer().isPresent());
    Assertions.assertTrue(msg.footer().get().contains("Closes #42"));
  }

  @Test
  void parsesBreakingChangeFooter() {
    final String raw = "feat: change\n\nBody.\n\nBREAKING CHANGE: removed API";
    final CommitMessage msg = parser.parse(raw);

    Assertions.assertTrue(msg.footer().isPresent());
    Assertions.assertTrue(msg.footer().get().contains("BREAKING CHANGE:"));
  }

  @Test
  void parsesFooterOnlyNoBody() {
    final String raw = "fix: bug\nReviewed-by: Z";
    final CommitMessage msg = parser.parse(raw);

    Assertions.assertTrue(msg.footer().isPresent());
    Assertions.assertTrue(msg.body().isEmpty());
  }
}
