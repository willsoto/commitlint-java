package com.tiltedwindmills.commitlint.core.config;

public final class ConfigLoader {

  public CommitlintConfig load() {
    return DefaultConfig.conventional();
  }
}
